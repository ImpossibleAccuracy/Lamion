package com.application.lamion.server.security.auth

import com.application.lamion.contants.AuthConstants
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtSecurityContextRepository(
    private val authenticationManager: ReactiveAuthenticationManager,
) : ServerSecurityContextRepository {
    override fun save(
        exchange: ServerWebExchange,
        context: SecurityContext
    ): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val headers = exchange.request.headers

        val token = headers.getFirst(AuthConstants.AUTH_HEADER)
            ?: return Mono.empty()

        val auth = UsernamePasswordAuthenticationToken(token, token)

        return authenticationManager
            .authenticate(auth)
            .map { authentication ->
                SecurityContextImpl(
                    authentication
                )
            }
    }
}
