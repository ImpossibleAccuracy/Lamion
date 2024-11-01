package com.application.lamion.feature.auth.controller.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SignInRequest(
    @NotNull
    @NotBlank
    var email: String,

    @NotNull
    @NotBlank
    var password: String,
)