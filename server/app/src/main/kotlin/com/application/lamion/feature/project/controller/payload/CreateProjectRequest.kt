package com.application.lamion.feature.project.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateProjectRequest(
    @field:NotNull
    @field:NotBlank
    @field:JsonProperty("title")
    var title: String,

    @field:JsonProperty("description")
    var description: String?,
)
