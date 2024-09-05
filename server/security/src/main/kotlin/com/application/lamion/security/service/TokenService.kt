package com.application.lamion.security.service

import org.springframework.security.core.userdetails.UserDetails

interface TokenService {
    fun extractSubject(token: String): String?

    fun generateToken(user: UserDetails): String

    fun generateToken(user: UserDetails, extra: Map<String, Any?>): String
}