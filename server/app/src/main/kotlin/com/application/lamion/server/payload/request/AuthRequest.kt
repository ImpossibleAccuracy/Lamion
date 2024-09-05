package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AuthRequest(
    @NotNull
    @NotBlank
    var email: String,

    @NotNull
    @NotBlank
    var password: String,
)