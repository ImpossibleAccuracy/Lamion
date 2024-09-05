package com.application.lamion.security.auth

import com.application.lamion.security.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthenticationFilter(
    private val tokenService: TokenService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        val accountEmail = tokenService.extractSubject(authHeader)

        if (accountEmail != null && isNotLogged) {
            val userDetails = userDetailsService.loadUserByUsername(accountEmail)

            if (userDetails != null) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }

    private val isNotLogged: Boolean
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication == null || !authentication.isAuthenticated
        }
}