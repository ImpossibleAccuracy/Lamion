package com.lamion.feature.projects.feature.domain.service

import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.domain.model.FunctionDomain

interface FunctionService {
    suspend fun exists(project: ProjectDomain, ids: List<Id>): Boolean

    suspend fun list(project: ProjectDomain, page: Long): List<FunctionDomain.Partial>

    suspend fun search(
        project: ProjectDomain,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?,
    ): List<FunctionDomain.Detailed>
}