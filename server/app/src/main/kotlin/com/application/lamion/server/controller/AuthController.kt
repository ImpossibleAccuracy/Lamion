package com.application.lamion.server.controller

import com.application.lamion.data.service.AuthService
import com.application.lamion.server.mapper.toDto
import com.application.lamion.server.payload.dto.UserDto
import com.application.lamion.server.payload.request.AuthRequest
import com.application.lamion.server.payload.response.TokenResponse
import com.application.lamion.server.security.UserHolder
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class AuthController @Autowired constructor(
    private val authService: AuthService,
) {
    @GetMapping("/user")
    fun profile(): UserDto {
        return UserHolder.getUserOrThrow().toDto()
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody data: AuthRequest): TokenResponse =
        authService
            .signIn(data.email, data.password)
            .let {
                TokenResponse(
                    user = it.user.toDto(),
                    token = it.token,
                )
            }

    @PostMapping("/register")
    fun register(@Valid @RequestBody data: AuthRequest): TokenResponse =
        authService
            .signUp(data.email, data.password)
            .let {
                TokenResponse(
                    user = it.user.toDto(),
                    token = it.token,
                )
            }
}
