package com.lamion.feature.auth.domain.service

import com.lamion.feature.auth.domain.model.AuthResult

interface OAuthService {
    suspend fun authWithGithub(code: String): AuthResult
}