package com.application.lamion.feature.auth.controller

import com.application.lamion.domain.service.resource.ResourceManager
import com.application.lamion.feature.auth.controller.mapper.toResponse
import com.application.lamion.feature.auth.controller.payload.request.SignInRequest
import com.application.lamion.feature.auth.controller.payload.request.SignUpRequest
import com.application.lamion.feature.auth.controller.payload.response.AuthResponse
import com.application.lamion.feature.auth.domain.service.AuthService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    private val authService: AuthService,
    private val resourceManager: ResourceManager,
) {
    @PostMapping("/signIn")
    suspend fun signIn(@Valid @RequestBody data: SignInRequest): AuthResponse =
        authService
            .signIn(
                email = data.email,
                password = data.password
            )
            .toResponse(resourceManager)

    @PostMapping("/signUp")
    suspend fun signUp(@Valid @RequestBody data: SignUpRequest): AuthResponse =
        authService
            .signUp(
                username = data.username,
                email = data.email,
                password = data.password
            )
            .toResponse(resourceManager)
}

