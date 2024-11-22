package com.lamion.feature.projects.feature.controller.payload.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

@Validated
data class CreateFeatureRequest(
    @JsonProperty("title")
    @field:NotBlank
    val title: String,

    @JsonProperty("description")
    @field:NotBlank
    val description: String,

    @JsonProperty("functions")
    val functions: List<@NotNull Id?>,
)
