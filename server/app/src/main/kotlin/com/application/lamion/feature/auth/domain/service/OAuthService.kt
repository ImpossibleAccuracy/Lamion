package com.application.lamion.feature.auth.domain.service

import com.application.lamion.feature.auth.domain.model.AuthResult

interface OAuthService {
    suspend fun authWithGithub(code: String): AuthResult

    suspend fun authWithGoogle(code: String): AuthResult
}