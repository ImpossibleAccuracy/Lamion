package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateEventRequest(
    @NotNull
    @NotBlank
    var title: String
)