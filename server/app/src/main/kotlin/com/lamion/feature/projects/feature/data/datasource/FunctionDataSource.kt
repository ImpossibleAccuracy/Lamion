package com.lamion.feature.projects.feature.data.datasource

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FunctionTagTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.data.database.utils.allAnd
import com.lamion.data.database.utils.exists
import com.lamion.domain.model.Id
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.projects.feature.domain.model.FunctionDomain
import org.jetbrains.exposed.sql.ExpressionAlias
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll

object FunctionDataSource {
    fun exists(projectId: Id, ids: List<Id>): Boolean =
        com.lamion.data.database.table.project.FunctionTable
            .select(com.lamion.data.database.table.project.FunctionTable.id)
            .where(
                com.lamion.data.database.table.project.FunctionTable.project.eq(projectId)
                    .and(com.lamion.data.database.table.project.FunctionTable.id.inList(ids))
            )
            .exists()

    fun list(projectId: Id, limit: Int, offset: Long): List<FunctionDomain.Partial> =
        com.lamion.data.database.table.project.FunctionTable
            .selectAll()
            .where(com.lamion.data.database.table.project.FunctionTable.project.eq(projectId))
            .limit(limit, offset)
            .toList()
            .map {
                FunctionDomain.Partial(
                    id = it[com.lamion.data.database.table.project.FunctionTable.id].value,
                    title = it[com.lamion.data.database.table.project.FunctionTable.title],
                )
            }

    fun search(
        totalEventsQuery: ExpressionAlias<Long>,
        projectId: Id,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?
    ) = com.lamion.data.database.table.project.FunctionTable
        .leftJoin(EventTable)
        .leftJoin(FeatureFunctionRef)
        .leftJoin(com.lamion.data.database.table.project.FeatureTable)
        .leftJoin(com.lamion.data.database.table.refs.FunctionTagRef)
        .select(
            totalEventsQuery,
            *com.lamion.data.database.table.project.FunctionTable.columns.toTypedArray(),
        )
        .where {
            allAnd(
                com.lamion.data.database.table.project.FunctionTable.project.eq(projectId),
                globalSearch?.let {
                    com.lamion.data.database.table.project.FunctionTable.title.like("%$it%")
                        .or(com.lamion.data.database.table.project.FeatureTable.title.like("%$it%"))
                },
                name?.let {
                    com.lamion.data.database.table.project.FunctionTable.title.like("%$it%")
                },
                features?.let {
                    FeatureFunctionRef.feature.inList(it)
                },
                tags?.let {
                    com.lamion.data.database.table.refs.FunctionTagRef.tag.inList(it)
                }
            )
        }
        .groupBy(com.lamion.data.database.table.project.FunctionTable.id)

    fun getFeatures(functionId: Id): List<FeatureDomain.Partial> =
        com.lamion.data.database.table.project.FeatureTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(com.lamion.data.database.table.project.FunctionTable)
            .selectAll()
            .where(com.lamion.data.database.table.project.FunctionTable.id eq functionId)
            .toList()
            .map {
                FeatureDomain.Partial(
                    id = it[com.lamion.data.database.table.project.FeatureTable.id].value,
                    title = it[com.lamion.data.database.table.project.FeatureTable.title],
                    description = it[com.lamion.data.database.table.project.FeatureTable.description],
                )
            }

    fun getTags(functionId: Id): List<FunctionDomain.Detailed.Tag> =
        FunctionTagTable
            .innerJoin(com.lamion.data.database.table.refs.FunctionTagRef)
            .select(FunctionTagTable.columns)
            .where(com.lamion.data.database.table.refs.FunctionTagRef.function eq functionId)
            .toList()
            .map {
                FunctionDomain.Detailed.Tag(
                    id = it[FunctionTagTable.id].value,
                    title = it[FunctionTagTable.title],
                )
            }
}