package com.application.lamion.feature.projects.settings.data

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.settings.domain.model.TokenDomain
import com.application.lamion.feature.projects.settings.domain.service.ProjectTokenService
import org.springframework.stereotype.Service

@Service
class ProjectTokenServiceImpl : ProjectTokenService {
    override suspend fun getTokens(account: AccountDomain, project: ProjectDomain): List<TokenDomain> {
        /* Simple query */
        TODO("Not yet implemented")
    }
}