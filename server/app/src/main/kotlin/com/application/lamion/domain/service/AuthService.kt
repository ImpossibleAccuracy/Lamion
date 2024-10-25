package com.application.lamion.domain.service

import com.application.lamion.domain.model.AuthResult
import com.application.lamion.domain.model.Authorization

interface AuthService {
    suspend fun authUser(token: String): Authorization

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(username: String, email: String, password: String): AuthResult
}
