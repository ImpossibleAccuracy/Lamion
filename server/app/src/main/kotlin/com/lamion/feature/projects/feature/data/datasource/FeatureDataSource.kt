package com.lamion.feature.projects.feature.data.datasource

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FeatureTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.data.database.utils.new
import com.lamion.domain.model.Id
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList

object FeatureDataSource {
    fun createFeature(
        projectId: Id,
        title: String,
        description: String,
    ) = FeatureTable
        .new {
            it[this.title] = title
            it[this.description] = description
            it[this.project] = projectId
        }

    fun attachFunctionsToFeature(
        featureId: Id,
        functions: List<Id>
    ) {
        FeatureFunctionRef
            .batchInsert(
                data = functions,
                shouldReturnGeneratedValues = false
            ) {
                this[FeatureFunctionRef.feature] = featureId
                this[FeatureFunctionRef.function] = it
            }
    }

    fun countFeatures(projectId: Id) = FeatureTable
        .select(FeatureTable.id)
        .where(
            FeatureTable.project.eq(projectId)
                .and(FeatureTable.deleted.eq(false))
        )
        .count()

    fun findFeature(projectId: Id, featureId: Id) =
        FeatureTable
            .selectAll()
            .where(
                FeatureTable.project.eq(projectId)
                    .and(FeatureTable.id eq featureId)
                    .and(FeatureTable.deleted.eq(false))
            )
            .firstOrNull()

    fun countFeatureCountByIdIn(
        projectId: Id,
        featuresIds: List<Id>,
    ) = FeatureTable
        .select(FeatureTable.id)
        .where(
            FeatureTable.project.eq(projectId)
                .and(FeatureTable.id inList featuresIds)
                .and(FeatureTable.deleted.eq(false))
        )
        .count()

    fun findFeaturesOrderByEventsCount(
        projectId: Id,
        eventsCountQuery: Expression<Long>,
        start: LocalDateTime,
        end: LocalDateTime,
        count: Int
    ) = FeatureTable
        .innerJoin(FeatureFunctionRef)
        .innerJoin(FunctionTable)
        .innerJoin(EventTable)
        .select(
            eventsCountQuery,
            *FeatureTable.columns.toTypedArray(),
        )
        .where(
            FeatureTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
                .and(FeatureTable.deleted.eq(false))
                .and(FunctionTable.deleted.eq(false))
        )
        .groupBy(*FeatureTable.columns.toTypedArray())
        .orderBy(eventsCountQuery)
        .limit(count)
        .toList()

    fun getFeaturesList(
        projectId: Id,
        eventsCountQuery: Expression<Long>,
        functionsCountQuery: Expression<Long>,
        errorsCountQuery: Expression<Long>,
        orderStatement: Expression<*>,
        limit: Int,
        offset: Long,
    ) = FeatureTable
        .select(
            eventsCountQuery,
            functionsCountQuery,
            errorsCountQuery,
            *FeatureTable.columns.toTypedArray(),
        )
        .where(
            FeatureTable.project.eq(projectId)
                .and(FeatureTable.deleted.eq(false))
        )
        .groupBy(*FeatureTable.columns.toTypedArray())
        .orderBy(orderStatement, SortOrder.DESC)
        .limit(limit, offset)
        .toList()

    fun getTopFunctions(
        featureId: Id,
        totalFeatureEventsCount: Long,
        count: Int
    ): List<FeatureDomain.Detailed.TopFunction> {
        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        return FunctionTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(FeatureTable)
            .innerJoin(EventTable)
            .select(
                FunctionTable.id,
                FunctionTable.title,
                eventsCountQuery,
            )
            .where(
                FeatureTable.id.eq(featureId)
                    .and(FeatureTable.deleted.eq(false))
                    .and(FunctionTable.deleted.eq(false))
            )
            .limit(count)
            .groupBy(
                FunctionTable.id,
                FunctionTable.title,
            )
            .orderBy(eventsCountQuery, SortOrder.DESC)
            .toList()
            .map {
                val eventsCount = it[eventsCountQuery]

                FeatureDomain.Detailed.TopFunction(
                    id = it[FunctionTable.id].value,
                    title = it[FunctionTable.title],
                    totalEvents = eventsCount,
                    percent = eventsCount * 100.0 / totalFeatureEventsCount
                )
            }
    }

    fun updateFeature(
        featureId: Id,
        title: String,
        description: String
    ) = FeatureTable
        .updateReturning(where = { FeatureTable.id eq featureId }) {
            it[FeatureTable.title] = title
            it[FeatureTable.description] = description
        }
        .first()

    fun deleteFeature(featureId: Id) =
        FeatureTable.update(where = { FeatureTable.id eq featureId }) {
            it[deleted] = true
        }
}