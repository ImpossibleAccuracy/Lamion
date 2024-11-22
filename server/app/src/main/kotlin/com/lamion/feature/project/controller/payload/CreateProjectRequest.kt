package com.lamion.feature.project.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateProjectRequest(
    @field:NotNull
    @field:NotBlank
    @JsonProperty("title")
    var title: String,

    @JsonProperty("description")
    var description: String?,
)
