package com.application.lamion.data.service

import com.application.lamion.data.properties.TokenProperties
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    private val tokenProperties: TokenProperties,
) {
    companion object {
        private const val CLAIM_NAME = "AUTH_CLAIM"
    }

    suspend fun extractSubject(token: String): String? = withContext(Dispatchers.Default) {
        try {
            JWT.decode(token).getClaim(CLAIM_NAME).asString()
        } catch (e: JWTDecodeException) {
            null
        }
    }

    suspend fun generateToken(subject: String, extra: Map<String, Any?> = mapOf()): String =
        withContext(Dispatchers.Default) {
            JWT.create()
                .withAudience(tokenProperties.audience)
                .withIssuer(tokenProperties.issuer)
                .withClaim(CLAIM_NAME, subject)
                .withExpiresAt(Date(System.currentTimeMillis() + tokenProperties.ttl))
                .sign(Algorithm.HMAC256(tokenProperties.secret))
        }
}