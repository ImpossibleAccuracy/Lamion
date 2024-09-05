package com.application.lamion.data.service

import com.application.lamion.security.service.TokenService
import com.application.lamion.domain.exception.OperationRejectedException
import com.application.lamion.domain.exception.ResourceNotFoundException
import com.application.lamion.domain.model.UserDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

private const val AUTH_FAILURE_MESSAGE = "User with such credentials not found"

@Service
class AuthService @Autowired constructor(
    private val userService: UserService,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signIn(
        email: String,
        password: String,
    ): AuthResult {
        val user = userService.findByEmail(email)
            ?: throw ResourceNotFoundException(AUTH_FAILURE_MESSAGE)

        if (passwordEncoder.matches(password, user.passwordHash)) {
            val token = tokenService.generateToken(user)
            return AuthResult(user, token)
        } else {
            throw ResourceNotFoundException(AUTH_FAILURE_MESSAGE)
        }
    }

    fun signUp(
        email: String,
        password: String,
    ): AuthResult {
        if (userService.existsByEmail(email)) {
            throw OperationRejectedException("User with such credentials already exists")
        }

        val user = UserDomain(
            email = email,
            passwordHash = passwordEncoder.encode(password)
        ).let {
            userService.create(it)
        }

        val token = tokenService.generateToken(user)

        return AuthResult(user, token)
    }

    data class AuthResult(
        val user: UserDomain,
        val token: String,
    )
}