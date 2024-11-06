package com.application.lamion.feature.projects.feature.data.datasource

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.project.FunctionTagTable
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.data.database.table.refs.FunctionTagRef
import com.application.lamion.data.database.utils.allAnd
import com.application.lamion.data.database.utils.exists
import com.application.lamion.domain.model.Id
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.application.lamion.feature.projects.feature.domain.model.FunctionDomain
import org.jetbrains.exposed.sql.ExpressionAlias
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll

object FunctionDataSource {
    fun exists(projectId: Id, ids: List<Id>): Boolean =
        FunctionTable
            .select(FunctionTable.id)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(FunctionTable.id.inList(ids))
            )
            .exists()

    fun list(projectId: Id, limit: Int, offset: Long): List<FunctionDomain.Partial> =
        FunctionTable
            .selectAll()
            .where(FunctionTable.project.eq(projectId))
            .limit(limit, offset)
            .toList()
            .map {
                FunctionDomain.Partial(
                    id = it[FunctionTable.id].value,
                    title = it[FunctionTable.title],
                )
            }

    fun search(
        totalEventsQuery: ExpressionAlias<Long>,
        projectId: Id,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?
    ) = FunctionTable
        .leftJoin(EventTable)
        .leftJoin(FeatureFunctionRef)
        .leftJoin(FeatureTable)
        .leftJoin(FunctionTagRef)
        .select(
            totalEventsQuery,
            *FunctionTable.columns.toTypedArray(),
        )
        .where {
            allAnd(
                FunctionTable.project.eq(projectId),
                globalSearch?.let {
                    FunctionTable.title.like("%$it%")
                        .or(FeatureTable.title.like("%$it%"))
                },
                name?.let {
                    FunctionTable.title.like("%$it%")
                },
                features?.let {
                    FeatureFunctionRef.feature.inList(it)
                },
                tags?.let {
                    FunctionTagRef.tag.inList(it)
                }
            )
        }
        .groupBy(FunctionTable.id)

    fun getFeatures(functionId: Id): List<FeatureDomain.Partial> =
        FeatureTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(FunctionTable)
            .selectAll()
            .where(FunctionTable.id eq functionId)
            .toList()
            .map {
                FeatureDomain.Partial(
                    id = it[FeatureTable.id].value,
                    title = it[FeatureTable.title],
                    description = it[FeatureTable.description],
                )
            }

    fun getTags(functionId: Id): List<FunctionDomain.Detailed.Tag> =
        FunctionTagTable
            .innerJoin(FunctionTagRef)
            .select(FunctionTagTable.columns)
            .where(FunctionTagRef.function eq functionId)
            .toList()
            .map {
                FunctionDomain.Detailed.Tag(
                    id = it[FunctionTagTable.id].value,
                    title = it[FunctionTagTable.title],
                )
            }
}