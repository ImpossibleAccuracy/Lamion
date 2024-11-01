package com.application.lamion.feature.shared.payload.dto

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty

data class ProjectDto(
    @JsonProperty("id")
    val id: Id,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("description")
    val description: String?,
)
