package com.application.lamion.feature.auth.domain.service

import com.application.lamion.domain.service.security.SecurityService
import com.application.lamion.feature.auth.domain.model.AuthResult

interface AuthService : SecurityService {
    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(username: String, email: String, password: String): AuthResult

}
