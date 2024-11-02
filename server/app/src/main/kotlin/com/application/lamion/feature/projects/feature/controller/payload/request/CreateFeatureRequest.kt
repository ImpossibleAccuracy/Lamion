package com.application.lamion.feature.projects.feature.controller.payload.request

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class CreateFeatureRequest(
    @field:JsonProperty("title")
    @field:NotBlank
    val title: String,

    @field:JsonProperty("description")
    @field:NotBlank
    val description: String,

    @field:JsonProperty("functions")
    @field:NotBlank
    val functions: List<Id>,
)
