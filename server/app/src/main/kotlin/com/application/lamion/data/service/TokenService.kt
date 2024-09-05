package com.application.lamion.data.service

import com.application.lamion.security.service.TokenService
import com.application.lamion.domain.properties.TokenProperties
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService(
    private val tokenProperties: TokenProperties,
) : TokenService {
    override fun extractSubject(token: String): String? = try {
        JWT.decode(token).getClaim(tokenProperties.claimName).asString()
    } catch (e: JWTDecodeException) {
        null
    }

    override fun generateToken(user: UserDetails): String =
        generateToken(user, mapOf())

    override fun generateToken(user: UserDetails, extra: Map<String, Any?>): String =
        JWT.create()
            .withAudience(tokenProperties.audience)
            .withIssuer(tokenProperties.issuer)
            .withClaim(tokenProperties.claimName, user.username.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + tokenProperties.ttl))
            .sign(Algorithm.HMAC256(tokenProperties.secret))
}