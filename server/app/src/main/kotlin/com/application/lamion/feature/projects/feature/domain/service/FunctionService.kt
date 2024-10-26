package com.application.lamion.feature.projects.feature.domain.service

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.feature.domain.model.FunctionDomain

interface FunctionService {
    suspend fun checkExists(ids: List<Id>): Boolean

    suspend fun list(project: ProjectDomain, page: Long): List<FunctionDomain.Partial>

    suspend fun search(
        project: ProjectDomain,
        globalSearch: String?,
        name: String?,
        features: List<Id>?,
        tags: List<Id>?,
    ): List<FunctionDomain.Detailed>
}