package com.application.lamion.feature.projects.settings.domain.service

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.settings.domain.model.AccessKeyDomain

interface ProjectAccessKeyService {
    suspend fun getAccessKeys(
        account: AccountDomain,
        project: ProjectDomain
    ): List<AccessKeyDomain>
}