package com.application.lamion.feature.projects.feature.data.service

import com.application.lamion.data.database.table.project.ErrorTable
import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.domain.model.*
import com.application.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.application.lamion.feature.projects.feature.data.datasource.FeatureDataSource
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.application.lamion.feature.projects.feature.domain.service.FeatureService
import com.application.lamion.feature.shared.utils.require
import com.application.lamion.utils.asyncDbQuery
import com.application.lamion.utils.dbQuery
import kotlinx.coroutines.awaitAll
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class FeatureServiceImpl : FeatureService {
    companion object {
        const val PAGE_SIZE = 50
        const val TOP_FUNCTIONS_COUNT = 3
    }

    override suspend fun create(
        project: ProjectDomain,
        account: AccountDomain,
        title: String,
        description: String,
        functions: List<Id>
    ): FeatureDomain.Partial = dbQuery {
        FeatureDataSource
            .createFeature(
                projectId = project.id,
                title = title,
                description = description
            )
            .require { "Feature not created" }
            .toDomainPartial()
            .also { feature ->
                FeatureDataSource.attachFunctionsToFeature(feature.id, functions)
            }
    }

    override suspend fun checkFeaturesExists(
        project: ProjectDomain,
        featuresIds: List<Id>
    ): Boolean = dbQuery {
        FeatureDataSource
            .countFeatureCountByIdIn(
                projectId = project.id,
                featuresIds = featuresIds,
            )
            .let { count ->
                count == featuresIds.size.toLong()
            }
    }

    override suspend fun get(
        id: Id,
        project: ProjectDomain
    ): FeatureDomain.Partial = dbQuery {
        FeatureDataSource
            .findFeature(
                projectId = project.id,
                featureId = id,
            )
            .require { "Feature not found" }
            .toDomainPartial()
    }


    override suspend fun getEventsGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        FeatureDataSource.getEventsGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun getTotalFeaturesCount(project: ProjectDomain): Long =
        dbQuery {
            FeatureDataSource.countFeatures(project.id)
        }

    override suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int
    ): ChartDomain<FeatureDomain.Partial, Long> = dbQuery {
        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        FeatureDataSource
            .findFeaturesOrderByEventsCount(
                projectId = project.id,
                eventsCountQuery = eventsCountQuery,
                start = dateRange.start,
                end = dateRange.end,
                count = count,
            )
            .associate {
                it.toDomainPartial() to it[eventsCountQuery]
            }
    }

    @Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")
    override suspend fun list(
        project: ProjectDomain,
        page: Long,
        sort: FeaturesSort,
    ): List<FeatureDomain.Detailed> =
        dbQuery {
            val eventsCountQuery = EventTable
                .innerJoin(FunctionTable)
                .innerJoin(FeatureFunctionRef)
                .select(EventTable.id.count())
                .where(
                    FeatureFunctionRef.feature.eq(FeatureTable.id)
                        .and(FunctionTable.deleted.eq(false))
                )
                .let {
                    wrapAsExpression<Long>(it)
                }
                .castTo(LongColumnType())
                .alias("eventsCount")

            val functionsCountQuery = FunctionTable
                .innerJoin(FeatureFunctionRef)
                .select(FunctionTable.id.count())
                .where(
                    FeatureFunctionRef.feature.eq(FeatureTable.id)
                        .and(FunctionTable.deleted.eq(false))
                )
                .let {
                    wrapAsExpression<Long>(it)
                }
                .castTo(LongColumnType())
                .alias("functionsCount")

            val errorsCountQuery = ErrorTable
                .innerJoin(FunctionTable)
                .innerJoin(FeatureFunctionRef)
                .select(ErrorTable.id.count())
                .where(
                    FeatureFunctionRef.feature.eq(FeatureTable.id)
                        .and(FunctionTable.deleted.eq(false))
                )
                .let {
                    wrapAsExpression<Long>(it)
                }
                .castTo(LongColumnType())
                .alias("errorsCount")

            FeatureDataSource
                .getFeaturesList(
                    projectId = project.id,
                    eventsCountQuery = eventsCountQuery,
                    functionsCountQuery = functionsCountQuery,
                    errorsCountQuery = errorsCountQuery,
                    orderStatement = when (sort) {
                        FeaturesSort.EVENTS_COUNT -> eventsCountQuery
                        FeaturesSort.ERRORS_COUNT -> errorsCountQuery
                        FeaturesSort.FUNCTIONS_COUNT -> functionsCountQuery
                        FeaturesSort.DATE_CREATED -> FeatureTable.createdAt
                    },
                    limit = PAGE_SIZE,
                    offset = page * PAGE_SIZE
                )
                .map {
                    asyncDbQuery {
                        val featureId = it[FeatureTable.id].value
                        val totalFeatureEvents = it[eventsCountQuery]

                        val topFunctions = FeatureDataSource.getTopFunctions(
                            featureId = featureId,
                            totalFeatureEventsCount = totalFeatureEvents,
                            count = TOP_FUNCTIONS_COUNT
                        )

                        FeatureDomain.Detailed(
                            id = featureId,
                            title = it[FeatureTable.title],
                            description = it[FeatureTable.description],
                            totalFunctions = it[functionsCountQuery],
                            totalEvents = totalFeatureEvents,
                            errors = it[errorsCountQuery],
                            topFunctions = topFunctions,
                        )
                    }
                }
                .awaitAll()
        }

    override suspend fun update(
        feature: FeatureDomain,
        account: AccountDomain,
        title: String,
        description: String
    ): FeatureDomain.Partial = dbQuery {
        FeatureDataSource.updateFeature(feature.id, title, description)
            .toDomainPartial()
    }

    override suspend fun delete(feature: FeatureDomain, account: AccountDomain): Unit = dbQuery {
        FeatureDataSource.deleteFeature(feature.id)
    }
}

fun ResultRow.toDomainPartial() = FeatureDomain.Partial(
    id = this[FeatureTable.id].value,
    title = this[FeatureTable.title],
    description = this[FeatureTable.description],
)
