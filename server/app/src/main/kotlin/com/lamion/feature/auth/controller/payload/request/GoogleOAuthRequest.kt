package com.lamion.feature.auth.controller.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class GoogleOAuthRequest(
    @field:NotNull
    @field:NotBlank
    val code: String
)