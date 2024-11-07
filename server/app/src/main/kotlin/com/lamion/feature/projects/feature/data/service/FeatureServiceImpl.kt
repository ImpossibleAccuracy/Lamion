package com.lamion.feature.projects.feature.data.service

import com.lamion.data.database.table.project.ErrorTable
import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FeatureTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.lamion.feature.projects.feature.data.datasource.FeatureDataSource
import com.lamion.feature.projects.feature.data.mapper.toFeatureDomainPartial
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.projects.feature.domain.service.FeatureService
import com.lamion.feature.shared.utils.require
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.coroutines.awaitAll
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
            .toFeatureDomainPartial()
    }

    override suspend fun delete(feature: FeatureDomain, account: AccountDomain): Unit = dbQuery {
        FeatureDataSource.deleteFeature(feature.id)
    }
}
