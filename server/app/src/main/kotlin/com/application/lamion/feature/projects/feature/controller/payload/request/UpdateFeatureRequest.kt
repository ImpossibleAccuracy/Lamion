package com.application.lamion.feature.projects.feature.controller.payload.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class UpdateFeatureRequest(
    @field:JsonProperty("title")
    @field:NotBlank
    val title: String,

    @field:JsonProperty("description")
    @field:NotBlank
    val description: String,
)
