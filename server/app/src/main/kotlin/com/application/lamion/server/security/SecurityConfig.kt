package com.application.lamion.server.security

import com.application.lamion.security.applyJwtSecurity
import com.application.lamion.security.service.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityConfig(
    private val tokenService: TokenService,
    private val userDetailsService: UserDetailsService,
    private val authenticationProvider: AuthenticationProvider
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {
                it.disable()
            }
            .csrf {
                it.disable()
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }

        http.applyJwtSecurity(tokenService, userDetailsService, authenticationProvider)

        return http.build()
    }
}