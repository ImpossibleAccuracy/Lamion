package com.application.lamion.feature.auth.data

import com.application.lamion.feature.auth.domain.model.AuthResult
import com.application.lamion.feature.auth.domain.service.OAuthService
import org.springframework.stereotype.Service

@Service
class OAuthServiceImpl : OAuthService {
    override suspend fun authWithGithub(code: String): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun authWithGoogle(code: String): AuthResult {
        TODO("Not yet implemented")
    }
}