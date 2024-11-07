package com.lamion.feature.projects.settings.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.AccessKeysDto

data class ProjectSettingsResponse(
    @JsonProperty("title")
    val title: String,

    @JsonProperty("description")
    val description: String?,

    @JsonProperty("tokens")
    val accessKeys: List<AccessKeysDto>,
)