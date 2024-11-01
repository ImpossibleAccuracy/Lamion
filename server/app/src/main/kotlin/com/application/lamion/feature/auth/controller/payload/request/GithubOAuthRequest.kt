package com.application.lamion.feature.auth.controller.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class GithubOAuthRequest(
    @NotNull
    @NotBlank
    val code: String
)