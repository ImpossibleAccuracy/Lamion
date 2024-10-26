package com.application.lamion.domain.service

import com.application.lamion.domain.security.Authorization

interface SecurityService {
    suspend fun authUser(token: String): Authorization
}