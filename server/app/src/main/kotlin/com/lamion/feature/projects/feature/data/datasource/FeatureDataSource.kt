package com.lamion.feature.projects.feature.data.datasource

import com.lamion.data.database.table.project.ErrorTable
import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.data.database.utils.new
import com.lamion.domain.model.Id
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object FeatureDataSource {
    fun createFeature(
        projectId: Id,
        title: String,
        description: String,
    ) = com.lamion.data.database.table.project.FeatureTable
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

    fun countFeatures(projectId: Id) = com.lamion.data.database.table.project.FeatureTable
        .select(com.lamion.data.database.table.project.FeatureTable.id)
        .where(
            com.lamion.data.database.table.project.FeatureTable.project.eq(projectId)
                .and(com.lamion.data.database.table.project.FeatureTable.deleted.eq(false))
        )
        .count()

    fun findFeature(projectId: Id, featureId: Id) =
        com.lamion.data.database.table.project.FeatureTable
            .selectAll()
            .where(
                com.lamion.data.database.table.project.FeatureTable.project.eq(projectId)
                    .and(com.lamion.data.database.table.project.FeatureTable.id eq featureId)
                    .and(com.lamion.data.database.table.project.FeatureTable.deleted.eq(false))
            )
            .firstOrNull()

    fun countFeatureCountByIdIn(
        projectId: Id,
        featuresIds: List<Id>,
    ) = com.lamion.data.database.table.project.FeatureTable
        .select(com.lamion.data.database.table.project.FeatureTable.id)
        .where(
            com.lamion.data.database.table.project.FeatureTable.project.eq(projectId)
                .and(com.lamion.data.database.table.project.FeatureTable.id inList featuresIds)
                .and(com.lamion.data.database.table.project.FeatureTable.deleted.eq(false))
        )
        .count()


    fun getEventsGroupByDate(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<LocalDate, Long> {
        val dateQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType())
        val countQuery = EventTable.id.count()

        return EventTable
            .innerJoin(com.lamion.data.database.table.project.FunctionTable)
            .select(dateQuery, countQuery)
            .where(
                com.lamion.data.database.table.project.FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
                    .and(com.lamion.data.database.table.project.FunctionTable.deleted.eq(false))
            )
            .groupBy(dateQuery)
            .orderBy(dateQuery)
            .toList()
            .associate {
                it[dateQuery] to it[countQuery]
            }
    }

    fun findFeaturesOrderByEventsCount(
        projectId: Id,
        eventsCountQuery: Expression<Long>,
        start: LocalDateTime,
        end: LocalDateTime,
        count: Int
    ) = com.lamion.data.database.table.project.FeatureTable
        .innerJoin(FeatureFunctionRef)
        .innerJoin(com.lamion.data.database.table.project.FunctionTable)
        .innerJoin(EventTable)
        .select(
            eventsCountQuery,
            *com.lamion.data.database.table.project.FeatureTable.columns.toTypedArray(),
        )
        .where(
            com.lamion.data.database.table.project.FeatureTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
                .and(com.lamion.data.database.table.project.FeatureTable.deleted.eq(false))
                .and(com.lamion.data.database.table.project.FunctionTable.deleted.eq(false))
        )
        .groupBy(*com.lamion.data.database.table.project.FeatureTable.columns.toTypedArray())
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
    ) = com.lamion.data.database.table.project.FeatureTable
        .innerJoin(FeatureFunctionRef)
        .innerJoin(com.lamion.data.database.table.project.FunctionTable)
        .innerJoin(EventTable)
        .innerJoin(ErrorTable)
        .select(
            eventsCountQuery,
            functionsCountQuery,
            errorsCountQuery,
            *com.lamion.data.database.table.project.FeatureTable.columns.toTypedArray(),
        )
        .where(
            com.lamion.data.database.table.project.FeatureTable.project.eq(projectId)
                .and(com.lamion.data.database.table.project.FeatureTable.deleted.eq(false))
                .and(com.lamion.data.database.table.project.FunctionTable.deleted.eq(false))
        )
        .groupBy(*com.lamion.data.database.table.project.FeatureTable.columns.toTypedArray())
        .orderBy(orderStatement, SortOrder.DESC)
        .limit(limit, offset)
        .toList()

    fun getTopFunctions(
        featureId: Id,
        totalFeatureEventsCount: Long,
        count: Int
    ): List<FeatureDomain.Detailed.TopFunction> {
        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        return com.lamion.data.database.table.project.FunctionTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(com.lamion.data.database.table.project.FeatureTable)
            .innerJoin(EventTable)
            .select(
                com.lamion.data.database.table.project.FunctionTable.id,
                com.lamion.data.database.table.project.FunctionTable.title,
                eventsCountQuery,
            )
            .where(
                com.lamion.data.database.table.project.FeatureTable.id.eq(featureId)
                    .and(com.lamion.data.database.table.project.FeatureTable.deleted.eq(false))
                    .and(com.lamion.data.database.table.project.FunctionTable.deleted.eq(false))
            )
            .limit(count)
            .groupBy(
                com.lamion.data.database.table.project.FunctionTable.id,
                com.lamion.data.database.table.project.FunctionTable.title,
            )
            .toList()
            .map {
                val eventsCount = it[eventsCountQuery]

                FeatureDomain.Detailed.TopFunction(
                    id = it[com.lamion.data.database.table.project.FunctionTable.id].value,
                    title = it[com.lamion.data.database.table.project.FunctionTable.title],
                    totalEvents = eventsCount,
                    percent = eventsCount * 100.0 / totalFeatureEventsCount
                )
            }
    }

    fun updateFeature(
        featureId: Id,
        title: String,
        description: String
    ) = com.lamion.data.database.table.project.FeatureTable
        .updateReturning(where = { com.lamion.data.database.table.project.FeatureTable.id eq featureId }) {
            it[com.lamion.data.database.table.project.FeatureTable.title] = title
            it[com.lamion.data.database.table.project.FeatureTable.description] = description
        }
        .first()

    fun deleteFeature(featureId: Id) =
        com.lamion.data.database.table.project.FeatureTable.update(where = { com.lamion.data.database.table.project.FeatureTable.id eq featureId }) {
            it[deleted] = true
        }
}