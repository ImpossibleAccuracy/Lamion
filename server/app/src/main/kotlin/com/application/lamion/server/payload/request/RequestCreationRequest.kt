package com.application.lamion.server.payload.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class RequestCreationRequest(
    @field:Positive
    var appId: Int,

    @field:NotNull
    @field:NotBlank
    var event: String,

    @field:NotNull
    @field:NotBlank
    var device: String,
)
