package com.lamion.domain.service.security

import com.lamion.domain.security.Authorization

interface SecurityService {
    suspend fun authUser(token: String): Authorization
}