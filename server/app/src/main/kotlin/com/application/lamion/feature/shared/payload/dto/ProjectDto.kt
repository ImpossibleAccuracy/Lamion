package com.application.lamion.feature.shared.payload.dto

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty

data class ProjectDto(
    @field:JsonProperty("id")
    val id: Id,

    @field:JsonProperty("title")
    val title: String,

    @field:JsonProperty("description")
    val description: String?,
)
