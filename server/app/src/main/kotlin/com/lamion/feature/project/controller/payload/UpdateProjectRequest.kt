package com.lamion.feature.project.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.server.validation.NullOrNotBlank

data class UpdateProjectRequest(
    @field:NullOrNotBlank
    @JsonProperty("title")
    var title: String? = null,

    @JsonProperty("description")
    var description: String? = null,
)
