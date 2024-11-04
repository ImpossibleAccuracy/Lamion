package com.application.lamion.feature.projects.feature.data.datasource

import com.application.lamion.data.database.table.ProjectTable
import com.application.lamion.data.database.table.project.*
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.data.database.utils.new
import com.application.lamion.domain.model.Id
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
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
        .where(FeatureTable.project eq projectId)
        .count()

    fun findFeature(projectId: Id, featureId: Id) =
        FeatureTable
            .selectAll()
            .where(
                (FeatureTable.project eq projectId) and
                        (FeatureTable.id eq featureId)
            )
            .firstOrNull()

    fun countFeatureCountByIdIn(
        projectId: Id,
        featuresIds: List<Id>,
    ) = FeatureTable
        .select(FeatureTable.id)
        .where(
            (FeatureTable.project eq projectId) and
                    (FeatureTable.id inList featuresIds)
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
            .innerJoin(FunctionTable)
            .select(dateQuery, countQuery)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
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
        eventsCountQuery: ExpressionAlias<Long>,
        start: LocalDateTime,
        end: LocalDateTime,
        count: Int
    ) = FeatureTable
        .innerJoin(FeatureFunctionRef)
        .innerJoin(FeatureTable)
        .innerJoin(EventTable)
        .select(
            eventsCountQuery,
            *FeatureTable.columns.toTypedArray(),
        )
        .where(
            (FeatureTable.project.eq(projectId))
                .and(EventTable.createdAt.between(start, end))
        )
        .groupBy(*FeatureTable.columns.toTypedArray())
        .orderBy(eventsCountQuery)
        .limit(count)
        .toList()

    fun getFeaturesList(
        projectId: Id,
        eventsCountQuery: ExpressionAlias<Long>,
        functionsCountQuery: ExpressionAlias<Long>,
        errorsCountQuery: ExpressionAlias<Long>,
        orderStatement: Expression<*>,
        limit: Int,
        offset: Long,
    ) = FeatureTable
        .innerJoin(ProjectTable)
        .innerJoin(FeatureFunctionRef)
        .innerJoin(FunctionTable)
        .innerJoin(EventTable)
        .innerJoin(UserTable)
        .innerJoin(ErrorTable)
        .select(
            eventsCountQuery,
            functionsCountQuery,
            errorsCountQuery,
            *FeatureTable.columns.toTypedArray(),
        )
        .where(
            (FeatureTable.project eq projectId)
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
            .innerJoin(EventTable)
            .selectAll()
            .where(FeatureFunctionRef.feature eq featureId)
            .limit(count)
            .toList()
            .map {
                val eventsCount = it[eventsCountQuery]

                FeatureDomain.Detailed.TopFunction(
                    id = it[FunctionTable.id].value,
                    title = it[FunctionTable.title],
                    totalEvents = eventsCount,
                    percent = totalFeatureEventsCount * 100.0 / eventsCount
                )
            }
    }

    fun updateFeature(
        featureId: Id,
        title: String,
        description: String
    ) = FeatureTable
        .updateReturning(
            returning = FeatureTable.columns,
            where = { FeatureTable.id eq featureId }
        ) {
            it[FeatureTable.title] = title
            it[FeatureTable.description] = description
        }
        .first()

    fun deleteFeature(featureId: Id) =
        FeatureTable.deleteWhere { FeatureTable.id eq featureId }
}