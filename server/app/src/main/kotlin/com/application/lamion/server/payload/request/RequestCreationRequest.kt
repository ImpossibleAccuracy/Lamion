package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class RequestCreationRequest(
    @Positive
    var appId: Int,

    @NotNull
    @NotBlank
    var event: String,

    @NotNull
    @NotBlank
    var device: String,
)
