package com.application.lamion.feature.project.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateProjectRequest(
    @NotNull
    @NotBlank
    @JsonProperty("title")
    var title: String,

    @JsonProperty("description")
    var description: String?,
)
