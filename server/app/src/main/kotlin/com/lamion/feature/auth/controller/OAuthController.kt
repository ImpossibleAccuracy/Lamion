package com.lamion.feature.auth.controller

import com.lamion.domain.service.resource.ResourceManager
import com.lamion.feature.auth.controller.mapper.toResponse
import com.lamion.feature.auth.controller.payload.request.GithubOAuthRequest
import com.lamion.feature.auth.controller.payload.request.GoogleOAuthRequest
import com.lamion.feature.auth.domain.service.OAuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth")
class OAuthController(
    private val oauthService: OAuthService,
    private val resourceManager: ResourceManager,
) {
    @PostMapping("/github")
    suspend fun githubOAuth(@RequestBody @Valid body: GithubOAuthRequest): com.lamion.feature.auth.controller.payload.response.AuthResponse =
        oauthService.authWithGithub(body.code).toResponse(resourceManager)

    @PostMapping("/google")
    suspend fun googleOAuth(@RequestBody @Valid body: GoogleOAuthRequest): com.lamion.feature.auth.controller.payload.response.AuthResponse =
        oauthService.authWithGithub(body.code).toResponse(resourceManager)
}