package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateAppRequest(
    @NotNull
    @NotBlank
    val title: String,

    @NotNull
    @NotBlank
    val description: String,
)