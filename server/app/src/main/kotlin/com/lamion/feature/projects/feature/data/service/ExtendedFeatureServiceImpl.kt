package com.lamion.feature.projects.feature.data.service

import com.lamion.data.database.table.project.*
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.data.database.table.refs.FunctionTagRef
import com.lamion.data.service.event.EventDataSource
import com.lamion.domain.model.*
import com.lamion.feature.projects.activity.data.datasource.ActivityDataSource
import com.lamion.feature.projects.feature.controller.payload.request.DefaultSort
import com.lamion.feature.projects.feature.data.datasource.FeatureDataSource
import com.lamion.feature.projects.feature.data.mapper.toFeatureDomainPartial
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.projects.feature.domain.service.ExtendedFeatureService
import com.lamion.feature.shared.utils.require
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.coroutines.awaitAll
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class ExtendedFeatureServiceImpl : ExtendedFeatureService {
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
            .toFeatureDomainPartial()
            .also { feature ->
                FeatureDataSource.attachFunctionsToFeature(feature.id, functions)
            }
    }

    override suspend fun exists(
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

    override suspend fun count(project: ProjectDomain): Long =
        dbQuery {
            FeatureDataSource.countFeatures(project.id)
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
            .toFeatureDomainPartial()
    }

    override suspend fun getFeatureTags(feature: FeatureDomain): List<String> = dbQuery {
        FunctionTagTable
            .innerJoin(FunctionTagRef)
            .innerJoin(FunctionTable)
            .innerJoin(FeatureFunctionRef)
            .select(FunctionTagTable.title)
            .where(
                FunctionTable.deleted.eq(false)
                    .and(FeatureFunctionRef.feature.eq(feature.id))
            )
            .toList()
            .map { it[FunctionTagTable.title] }
    }

    override suspend fun list(
        project: ProjectDomain,
        page: Long,
        sort: DefaultSort,
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
                        DefaultSort.EVENTS_COUNT -> eventsCountQuery
                        DefaultSort.ERRORS_COUNT -> errorsCountQuery
                        DefaultSort.FUNCTIONS_COUNT -> functionsCountQuery
                        DefaultSort.DATE_CREATED -> FeatureTable.createdAt
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
            .toFeatureDomainPartial()
    }

    override suspend fun delete(feature: FeatureDomain, account: AccountDomain): Unit = dbQuery {
        FeatureDataSource.deleteFeature(feature.id)
    }

    override suspend fun getTopFeaturesList(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int
    ): List<FeatureWithEvents> = dbQuery {
        val totalEventsCount = EventDataSource.countEventsByCreatedBetween(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )

        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        ActivityDataSource
            .getFeatureWithTotalEventsCount(
                eventsCountQuery = eventsCountQuery,
                projectId = project.id,
                start = dateRange.start,
                end = dateRange.end,
                count = count,
            )
            .map {
                val totalEventsResult = it[eventsCountQuery]

                FeatureWithEvents(
                    id = it[FeatureTable.id].value,
                    title = it[FeatureTable.title],
                    description = it[FeatureTable.description],
                    totalEvents = totalEventsResult,
                    totalEventsPercent = totalEventsResult * 100.0 / totalEventsCount
                )
            }
    }

    override suspend fun getTopFeaturesWithEventsCount(
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
            .toList()
            .associate {
                it.toFeatureDomainPartial() to it[eventsCountQuery]
            }
    }
}
