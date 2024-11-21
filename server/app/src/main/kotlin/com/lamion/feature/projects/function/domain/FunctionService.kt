package com.lamion.feature.projects.function.domain

import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.domain.model.FeatureDomain

interface FunctionService {
    suspend fun exists(project: ProjectDomain, ids: List<Id>): Boolean

    suspend fun list(project: ProjectDomain, page: Long): List<FunctionDomain.Partial>

    suspend fun listWithEventsCount(
        feature: FeatureDomain,
        query: String?,
        page: Long,
    ): Map<FunctionDomain.Partial, Long>

    suspend fun search(
        project: ProjectDomain,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?,
    ): List<FunctionDomain.Detailed>

    suspend fun detachFunction(feature: FeatureDomain, functionId: Id)
}