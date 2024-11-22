package com.lamion.feature.projects.settings.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class CreateAccessKeyRequest(
    @JsonProperty("title")
    @field:NotBlank
    val title: String
)