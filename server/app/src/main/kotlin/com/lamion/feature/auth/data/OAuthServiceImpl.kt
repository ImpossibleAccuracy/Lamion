package com.lamion.feature.auth.data

import com.lamion.feature.auth.domain.model.AuthResult
import com.lamion.feature.auth.domain.service.OAuthService
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