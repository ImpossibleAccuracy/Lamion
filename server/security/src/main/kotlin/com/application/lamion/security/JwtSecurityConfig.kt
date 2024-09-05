package com.application.lamion.security

import com.application.lamion.security.auth.JwtAuthenticationFilter
import com.application.lamion.security.service.TokenService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Throws(Exception::class)
fun HttpSecurity.applyJwtSecurity(
    tokenService: TokenService,
    userDetailsService: UserDetailsService,
    authenticationProvider: AuthenticationProvider
) {
    sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    authenticationProvider(authenticationProvider)
        .addFilterBefore(
            JwtAuthenticationFilter(tokenService, userDetailsService),
            UsernamePasswordAuthenticationFilter::class.java
        )
}
