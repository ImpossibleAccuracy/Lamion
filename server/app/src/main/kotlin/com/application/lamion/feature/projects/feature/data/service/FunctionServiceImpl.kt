package com.application.lamion.feature.projects.feature.data.service

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.project.FunctionTagTable
import com.application.lamion.data.database.table.refs.FeatureFunctionRef
import com.application.lamion.data.database.table.refs.FunctionTagRef
import com.application.lamion.data.database.utils.allAnd
import com.application.lamion.data.database.utils.exists
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.application.lamion.feature.projects.feature.domain.model.FunctionDomain
import com.application.lamion.feature.projects.feature.domain.service.FunctionService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FunctionServiceImpl : FunctionService {
    companion object {
        const val PAGE_SIZE = 30
    }

    override suspend fun checkExists(ids: List<Id>): Boolean {
        // TODO: verify
        return FunctionTable
            .select(FunctionTable.id)
            .where(FunctionTable.id inList ids)
            .exists()
    }

    override suspend fun list(project: ProjectDomain, page: Long): List<FunctionDomain.Partial> {
        return FunctionTable
            .selectAll()
            .limit(PAGE_SIZE, page * PAGE_SIZE)
            .toList()
            .map {
                FunctionDomain.Partial(
                    id = it[FunctionTable.id].value,
                    title = it[FunctionTable.title],
                )
            }
    }

    override suspend fun search(
        project: ProjectDomain,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?
    ): List<FunctionDomain.Detailed> = coroutineScope {
        val totalEventsQuery = EventTable.id.count().alias("totalEvents")

        FunctionTable
            .leftJoin(EventTable)
            .leftJoin(FeatureFunctionRef)
            .leftJoin(FunctionTagRef)
            .select(
                totalEventsQuery,
                *FunctionTable.columns.toTypedArray(),
            )
            .where {
                allAnd(
                    globalSearch?.let {
                        (FunctionTable.title like "%$it%") or
                                (FunctionTable.title like "%$it%")
                    },
                    name?.let {
                        FunctionTable.title like "%$it%"
                    },
                    features?.let {
                        FeatureFunctionRef.feature inList it
                    },
                    tags?.let {
                        FunctionTagRef.tag inList it
                    }
                )
            }
            .toList()
            .map {
                async {
                    val id = it[FunctionTable.id].value

                    val featuresDeferred = async { getFeatures(id) }
                    val tagsDeferred = async { getTags(id) }

                    FunctionDomain.Detailed(
                        id = id,
                        title = it[FunctionTable.title],
                        totalEvents = it[totalEventsQuery],
                        features = featuresDeferred.await(),
                        tags = tagsDeferred.await(),
                    )
                }
            }
            .awaitAll()
    }

    private fun getFeatures(functionId: Id): List<FeatureDomain.Partial> =
        FeatureTable
            .innerJoin(FeatureFunctionRef)
            .innerJoin(FunctionTable)
            .selectAll()
            .where(FunctionTable.id eq functionId)
            .toList()
            .map {
                FeatureDomain.Partial(
                    id = it[FeatureTable.id].value,
                    title = it[FeatureTable.title],
                    description = it[FeatureTable.description],
                )
            }


    private fun getTags(functionId: Id): List<FunctionDomain.Detailed.Tag> =
        FunctionTagTable
            .innerJoin(FunctionTagRef)
            .select(FunctionTagTable.columns)
            .where(FunctionTagRef.function eq functionId)
            .toList()
            .map {
                FunctionDomain.Detailed.Tag(
                    id = it[FunctionTagTable.id].value,
                    title = it[FunctionTagTable.title],
                )
            }
}
