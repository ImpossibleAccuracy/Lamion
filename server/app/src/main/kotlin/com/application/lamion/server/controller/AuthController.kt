package com.application.lamion.server.controller

import com.application.lamion.data.service.AuthServiceImpl
import com.application.lamion.server.mapper.toDto
import com.application.lamion.server.payload.dto.AccountDto
import com.application.lamion.server.payload.request.SignInRequest
import com.application.lamion.server.payload.request.SignUpRequest
import com.application.lamion.server.payload.response.TokenResponse
import com.application.lamion.server.security.UserHolder
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class AuthController @Autowired constructor(
    private val authService: AuthServiceImpl,
) {
    @GetMapping("/me")
    suspend fun profile(): AccountDto =
        UserHolder.requireAccount().toDto()

    @PostMapping("/signIn")
    suspend fun signIn(@Valid @RequestBody data: SignInRequest): TokenResponse =
        authService
            .signIn(
                email = data.email,
                password = data.password
            )
            .let {
                TokenResponse(
                    user = it.user.toDto(),
                    token = it.token,
                )
            }

    @PostMapping("/signUp")
    suspend fun signUp(@Valid @RequestBody data: SignUpRequest): TokenResponse =
        authService
            .signUp(
                username = data.username,
                email = data.email,
                password = data.password
            )
            .let {
                TokenResponse(
                    user = it.user.toDto(),
                    token = it.token,
                )
            }
}
