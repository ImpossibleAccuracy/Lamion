package com.application.lamion.domain.service.security

import com.application.lamion.domain.security.Authorization

interface SecurityService {
    suspend fun authUser(token: String): Authorization
}