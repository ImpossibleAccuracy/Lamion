package com.lamion.feature.projects.settings.domain.service

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.settings.domain.model.AccessKeyDomain

interface ProjectAccessKeyService {
    suspend fun getAccessKeys(
        account: AccountDomain,
        project: ProjectDomain
    ): List<AccessKeyDomain>
}