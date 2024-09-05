package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateAppRequest(
    @field:NotNull
    @field:NotBlank
    val title: String,

    @field:NotNull
    @field:NotBlank
    val description: String,
)