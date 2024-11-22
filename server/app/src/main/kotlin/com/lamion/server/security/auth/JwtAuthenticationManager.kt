package com.lamion.server.security.auth

import com.lamion.domain.security.Authorization
import com.lamion.domain.service.security.SecurityService
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
    private val securityService: SecurityService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> =
        mono {
            val jwtToken = authentication.credentials.toString()

            securityService
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
