package com.application.lamion.data.service

import com.application.lamion.domain.properties.TokenProperties
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    private val tokenProperties: TokenProperties,
) {
    companion object {
        private const val CLAIM_NAME = "AUTH_CLAIM"
    }

    fun extractSubject(token: String): String? = try {
        JWT.decode(token).getClaim(CLAIM_NAME).asString()
    } catch (e: JWTDecodeException) {
        null
    }

    fun generateToken(subject: String, extra: Map<String, Any?> = mapOf()): String =
        JWT.create()
            .withAudience(tokenProperties.audience)
            .withIssuer(tokenProperties.issuer)
            .withClaim(CLAIM_NAME, subject)
            .withExpiresAt(Date(System.currentTimeMillis() + tokenProperties.ttl))
            .sign(Algorithm.HMAC256(tokenProperties.secret))
}