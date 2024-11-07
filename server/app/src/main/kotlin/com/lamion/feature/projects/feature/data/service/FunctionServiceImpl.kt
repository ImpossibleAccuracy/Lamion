package com.lamion.feature.projects.feature.data.service

import com.lamion.data.database.table.project.EventTable
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.data.datasource.FunctionDataSource
import com.lamion.feature.projects.feature.domain.model.FunctionDomain
import com.lamion.feature.projects.feature.domain.service.FunctionService
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.count
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
        FunctionDataSource.list(project.id, PAGE_SIZE, page * PAGE_SIZE)
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
                        val id = it[com.lamion.data.database.table.project.FunctionTable.id].value

                        val featuresDeferred = asyncDbQuery { FunctionDataSource.getFeatures(id) }
                        val tagsDeferred = asyncDbQuery { FunctionDataSource.getTags(id) }

                        FunctionDomain.Detailed(
                            id = id,
                            title = it[com.lamion.data.database.table.project.FunctionTable.title],
                            totalEvents = it[totalEventsQuery],
                            features = featuresDeferred.await(),
                            tags = tagsDeferred.await(),
                        )
                    }
                }
                .awaitAll()
        }
    }
}
