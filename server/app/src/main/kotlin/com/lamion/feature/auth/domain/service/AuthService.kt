package com.lamion.feature.auth.domain.service

import com.lamion.domain.service.security.SecurityService
import com.lamion.feature.auth.domain.model.AuthResult

interface AuthService : SecurityService {
    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(username: String, email: String, password: String?): AuthResult

    suspend fun createUserWithOauth(
        username: String?,
        email: String,
    ): AuthResult
}
