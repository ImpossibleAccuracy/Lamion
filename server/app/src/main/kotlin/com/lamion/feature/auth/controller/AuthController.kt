package com.lamion.feature.auth.controller

import com.lamion.domain.service.resource.ResourceManager
import com.lamion.feature.auth.controller.mapper.toResponse
import com.lamion.feature.auth.controller.payload.request.SignInRequest
import com.lamion.feature.auth.controller.payload.request.SignUpRequest
import com.lamion.feature.auth.controller.payload.request.TokenRefreshRequest
import com.lamion.feature.auth.controller.payload.response.AuthResponse
import com.lamion.feature.auth.domain.service.AuthService
import com.lamion.feature.shared.controller.BaseController
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
) : BaseController() {
    @PostMapping("/signIn")
    suspend fun signIn(@Valid @RequestBody data: SignInRequest): AuthResponse =
        endpoint("Sign in") {
            authService
                .signIn(
                    email = data.email,
                    password = data.password
                )
                .toResponse(resourceManager)
        }

    @PostMapping("/signUp")
    suspend fun signUp(@Valid @RequestBody data: SignUpRequest): AuthResponse =
        endpoint("Sign up") {
            authService
                .signUp(
                    username = data.username,
                    email = data.email,
                    password = data.password
                )
                .toResponse(resourceManager)
        }

    @PostMapping("/refresh")
    suspend fun refresh(@Valid @RequestBody data: TokenRefreshRequest): AuthResponse =
        endpoint("Token refresh") {
            authService
                .authUser(data.token)
                .let {
                    authService.refresh(account)
                }
                .toResponse(resourceManager)
        }
}
