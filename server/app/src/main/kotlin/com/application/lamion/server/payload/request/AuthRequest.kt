package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AuthRequest(
    @field:NotNull
    @field:NotBlank
    var email: String,

    @field:NotNull
    @field:NotBlank
    var password: String,
)