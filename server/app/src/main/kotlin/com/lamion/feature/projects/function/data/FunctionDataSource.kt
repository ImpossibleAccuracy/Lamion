package com.lamion.feature.projects.function.data

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FeatureTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.project.FunctionTagTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.data.database.table.refs.FunctionTagRef
import com.lamion.data.database.utils.allAnd
import com.lamion.data.database.utils.exists
import com.lamion.domain.model.Id
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like

object FunctionDataSource {
    fun exists(projectId: Id, ids: List<Id>): Boolean =
        FunctionTable
            .select(FunctionTable.id)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(FunctionTable.id.inList(ids))
            )
            .exists()

    fun list(projectId: Id, limit: Int, offset: Long): List<ResultRow> =
        FunctionTable
            .selectAll()
            .where(FunctionTable.project.eq(projectId))
            .limit(limit, offset)
            .toList()

    fun listWithEventsCount(
        featureId: Id,
        nameLike: String?,
        limit: Int,
        offset: Long,
    ): Map<ResultRow, Long> {
        val subquery = EventTable
            .select(EventTable.id.count())
            .where(EventTable.function.eq(FunctionTable.id))
            .let {
                wrapAsExpression<Long>(it)
            }
            .castTo(LongColumnType())

        return FunctionTable
            .innerJoin(FeatureFunctionRef)
            .select(
                subquery,
                *FunctionTable.columns.toTypedArray(),
            )
            .where(
                allAnd(
                    FeatureFunctionRef.feature.eq(featureId),
                    FunctionTable.deleted.eq(false),
                    nameLike
                        ?.takeIf { it.isNotBlank() }
                        ?.let {
                            FunctionTable.title.lowerCase().like("%${it.lowercase()}%")
                        }
                )
            )
            .limit(limit, offset)
            .toList()
            .associateWith { it[subquery] }
    }

    fun search(
        totalEventsQuery: ExpressionAlias<Long>,
        projectId: Id,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?,
        limit: Int,
        offset: Long,
    ) = FunctionTable
        .leftJoin(EventTable)
        .leftJoin(FeatureFunctionRef)
        .leftJoin(FeatureTable)
        .leftJoin(FunctionTagRef)
        .leftJoin(FunctionTagTable)
        .select(
            totalEventsQuery,
            *FunctionTable.columns.toTypedArray(),
        )
        .where {
            allAnd(
                FunctionTable.project.eq(projectId),
                globalSearch
                    ?.takeIf { it.isNotBlank() }
                    ?.lowercase()
                    ?.let {
                        FunctionTable.title.lowerCase().like("%$it%")
                            .or(FeatureTable.title.lowerCase().like("%$it%"))
                            .or(FunctionTagTable.title.lowerCase().like("%$it%"))
                    },
                name
                    ?.takeIf { it.isNotBlank() }
                    ?.lowercase()
                    ?.let {
                        FunctionTable.title.lowerCase().like("%$it%")
                    },
                features
                    ?.takeIf { it.isNotEmpty() }
                    ?.let {
                        FeatureFunctionRef.feature.inList(it)
                    },
                tags
                    ?.takeIf { it.isNotEmpty() }
                    ?.let {
                        FunctionTagRef.tag.inList(it)
                    }
            )
        }
        .limit(limit, offset)
        .groupBy(FunctionTable.id)
        .toList()

    fun getFeatures(functionId: Id): List<ResultRow> =
        FeatureTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(FunctionTable)
            .selectAll()
            .where(FunctionTable.id eq functionId)
            .toList()

    fun getTags(functionId: Id): List<ResultRow> =
        FunctionTagTable
            .innerJoin(FunctionTagRef)
            .select(FunctionTagTable.columns)
            .where(FunctionTagRef.function eq functionId)
            .toList()
}