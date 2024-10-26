package com.application.lamion.feature.projects.settings.domain.service

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.settings.domain.model.TokenDomain

interface ProjectTokenService {
    suspend fun getTokens(
        account: AccountDomain,
        project: ProjectDomain
    ): List<TokenDomain>
}