package com.lamion.feature.auth.controller.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SignUpRequest(
    @field:NotNull
    @field:NotBlank
    var username: String,

    @field:NotNull
    @field:NotBlank
    var email: String,

    @field:NotNull
    @field:NotBlank
    var password: String,
)