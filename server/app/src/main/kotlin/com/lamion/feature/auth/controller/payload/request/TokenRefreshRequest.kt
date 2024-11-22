package com.lamion.feature.auth.controller.payload.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class TokenRefreshRequest(
    @JsonProperty("refreshToken")
    @field:NotNull
    @field:NotBlank
    var token: String,
)