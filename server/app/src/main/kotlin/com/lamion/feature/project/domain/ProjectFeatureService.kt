package com.lamion.feature.project.domain

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.project.ProjectService

interface ProjectFeatureService : ProjectService {
    suspend fun create(owner: AccountDomain, title: String, description: String?): ProjectDomain

    suspend fun list(account: AccountDomain): List<ProjectDomain>

    suspend fun update(
        project: ProjectDomain,
        account: AccountDomain,
        title: String?,
        description: String?,
    ): ProjectDomain

    suspend fun delete(project: ProjectDomain, account: AccountDomain)
}