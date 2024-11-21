package com.lamion.feature.projects.function.data

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FeatureTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.project.FunctionTagTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.projects.function.domain.FunctionDomain
import com.lamion.feature.projects.function.domain.FunctionService
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class FunctionServiceImpl : FunctionService {
    companion object {
        const val PAGE_SIZE = 30
    }

    override suspend fun exists(project: ProjectDomain, ids: List<Id>): Boolean = dbQuery {
        FunctionDataSource.exists(project.id, ids)
    }

    override suspend fun list(project: ProjectDomain, page: Long): List<FunctionDomain.Partial> = dbQuery {
        FunctionDataSource
            .list(project.id, PAGE_SIZE, page * PAGE_SIZE)
            .map {
                it.toPartial()
            }
    }

    override suspend fun listWithEventsCount(
        feature: FeatureDomain,
        query: String?,
        page: Long,
    ): Map<FunctionDomain.Partial, Long> =
        dbQuery {
            FunctionDataSource
                .listWithEventsCount(
                    featureId = feature.id,
                    nameLike = query,
                    limit = PAGE_SIZE,
                    offset = page * PAGE_SIZE
                )
                .map { (row, count) ->
                    row.toPartial() to count
                }
                .toMap()
        }

    override suspend fun search(
        project: ProjectDomain,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?
    ): List<FunctionDomain.Detailed> = dbQuery {
        coroutineScope {
            val totalEventsQuery = EventTable.id.count().alias("totalEvents")

            FunctionDataSource
                .search(
                    totalEventsQuery = totalEventsQuery,
                    projectId = project.id,
                    globalSearch = globalSearch,
                    name = name,
                    features = features,
                    tags = tags
                )
                .toList()
                .map {
                    async {
                        val id = it[FunctionTable.id].value

                        val featuresDeferred = asyncDbQuery {
                            FunctionDataSource
                                .getFeatures(id)
                                .map {
                                    FeatureDomain.Partial(
                                        id = it[FeatureTable.id].value,
                                        title = it[FeatureTable.title],
                                        description = it[FeatureTable.description],
                                    )
                                }
                        }

                        val tagsDeferred = asyncDbQuery {
                            FunctionDataSource
                                .getTags(id)
                                .map {
                                    FunctionDomain.Detailed.Tag(
                                        id = it[FunctionTagTable.id].value,
                                        title = it[FunctionTagTable.title],
                                    )
                                }
                        }

                        FunctionDomain.Detailed(
                            id = id,
                            title = it[FunctionTable.title],
                            events = it[totalEventsQuery],
                            features = featuresDeferred.await(),
                            tags = tagsDeferred.await(),
                        )
                    }
                }
                .awaitAll()
        }
    }

    override suspend fun detachFunction(feature: FeatureDomain, functionId: Id): Unit = dbQuery {
        FeatureFunctionRef
            .deleteWhere {
                FeatureFunctionRef.feature.eq(feature.id)
                    .and(function.eq(functionId))
            }
    }
}

private fun ResultRow.toPartial() = FunctionDomain.Partial(
    id = this[FunctionTable.id].value,
    title = this[FunctionTable.title],
)
