package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id

data class ProjectDto(
    @JsonProperty("id")
    val id: Id,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("description")
    val description: String?,
)
