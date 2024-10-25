package com.application.lamion.server.security.auth

import com.application.lamion.domain.service.AuthService
import com.application.lamion.domain.model.Authorization
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Lazy
@Component
class JwtAuthenticationManager(
    private val authService: AuthService
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        mono {
            val jwtToken = authentication.credentials.toString()

            authService
                .authUser(jwtToken)
                .let { getAuthorities(it) }
        }

    private fun getAuthorities(authorization: Authorization): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(
            /* principal = */ authorization,
            /* credentials = */ null,
            /* authorities = */ authorization.roles.map { SimpleGrantedAuthority(it.name) },
        )
    }
}
