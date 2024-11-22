package com.lamion.feature.projects.feature.controller.payload.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class UpdateFeatureRequest(
    @JsonProperty("title")
    @field:NotBlank
    val title: String,

    @JsonProperty("description")
    @field:NotBlank
    val description: String,
)
