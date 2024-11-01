package com.application.lamion.feature.projects.feature.data

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.data.database.utils.new
import com.application.lamion.domain.model.*
import com.application.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.application.lamion.feature.projects.feature.domain.service.FeatureService
import com.application.lamion.feature.shared.utils.require
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
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
    ): FeatureDomain.Partial =
        FeatureTable
            .new {
                it[FeatureTable.title] = title
                it[FeatureTable.description] = description
                it[FeatureTable.project] = project.id
            }
            .require { "Feature not created" }
            .toDomainPartial()
            .also { feature ->
                FeatureFunctionRef
                    .batchInsert(
                        data = functions,
                        shouldReturnGeneratedValues = false
                    ) {
                        this[FeatureFunctionRef.feature] = feature.id
                        this[FeatureFunctionRef.function] = it
                    }
            }

    override suspend fun checkFeaturesExists(project: ProjectDomain, featuresIds: List<Id>): Boolean =
        FeatureTable
            .select(FeatureTable.id)
            .where(
                (FeatureTable.project eq project.id) and
                        (FeatureTable.id inList featuresIds)
            )
            .count()
            .let { count ->
                count == featuresIds.size.toLong()
            }

    override suspend fun get(id: Id, project: ProjectDomain): FeatureDomain.Partial =
        FeatureTable
            .selectAll()
            .where(
                (FeatureTable.project eq project.id) and
                        (FeatureTable.id eq id)
            )
            .firstOrNull()
            .require { "Feature not found" }
            .toDomainPartial()

    override suspend fun getTotalEvents(project: ProjectDomain): ChartDomain<LocalDate, Long> {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalFeaturesCount(project: ProjectDomain): Long =
        FeatureTable
            .select(FeatureTable.id)
            .where(FeatureTable.project eq project.id)
            .count()

    // TODO: separate to several methods
    override suspend fun getTopFeatures(
        project: ProjectDomain,
        period: TimePeriod,
        count: Int
    ): Map<FeatureDomain.Partial, Long> {
        // TODO: compute from [period]
        val periodStartDate = now().toLocalDateTime(TimeZone.UTC)

        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        return FeatureTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(FeatureTable)
            .innerJoin(EventTable)
            .select(
                eventsCountQuery,
                *FeatureTable.columns.toTypedArray(),
            )
            .where {
                (FeatureTable.project eq project.id) and
                        (EventTable.createdAt greaterEq periodStartDate)
            }
            .groupBy(*FeatureTable.columns.toTypedArray())
            .orderBy(eventsCountQuery)
            .limit(count)
            .toList()
            .associate {
                it.toDomainPartial() to it[eventsCountQuery]
            }
    }

    override suspend fun list(project: ProjectDomain, page: Long, sort: FeaturesSort): List<FeatureDomain.Detailed> =
        coroutineScope {
            val eventsCountQuery = EventTable.id.count().alias("eventsCount")
            val functionsCountQuery = EventTable.id.count().alias("functionsCount")
            val errorsCountQuery = EventTable.id.count().alias("errorsCount")

            FeatureTable
                .innerJoin(FeatureFunctionRef)
                .innerJoin(FeatureTable)
                .innerJoin(EventTable)
                .select(
                    eventsCountQuery,
                    functionsCountQuery,
                    errorsCountQuery,
                    *FeatureTable.columns.toTypedArray(),
                )
                .where {
                    (FeatureTable.project eq project.id)
                }
                .groupBy(*FeatureTable.columns.toTypedArray())
                .orderBy(eventsCountQuery)
                .limit(PAGE_SIZE, page * PAGE_SIZE)
                .toList()
                .map {
                    async {
                        val featureId = it[FeatureTable.id].value
                        val totalFeatureEvents = it[eventsCountQuery]

                        FeatureDomain.Detailed(
                            id = featureId,
                            title = it[FeatureTable.title],
                            description = it[FeatureTable.description],
                            totalFunctions = it[functionsCountQuery],
                            totalEvents = totalFeatureEvents,
                            errors = it[errorsCountQuery],
                            topFunctions = getTopFunctions(
                                totalFeatureEventsCount = totalFeatureEvents,
                                featureId = featureId,
                                count = TOP_FUNCTIONS_COUNT
                            )
                        )
                    }
                }
                .awaitAll()
        }

    private fun getTopFunctions(
        totalFeatureEventsCount: Long,
        featureId: Id,
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

    override suspend fun update(
        feature: FeatureDomain,
        account: AccountDomain,
        title: String,
        description: String
    ): FeatureDomain.Partial =
        FeatureTable
            .updateReturning(
                returning = FeatureTable.columns,
                where = { FeatureTable.id eq feature.id }
            ) {
                it[FeatureTable.title] = title
                it[FeatureTable.description] = description
            }
            .first()
            .toDomainPartial()

    override suspend fun delete(feature: FeatureDomain, account: AccountDomain) {
        FeatureTable.deleteWhere { FeatureTable.id eq feature.id }
    }
}

fun ResultRow.toDomainPartial() = FeatureDomain.Partial(
    id = this[FeatureTable.id].value,
    title = this[FeatureTable.title],
    description = this[FeatureTable.description],
)
