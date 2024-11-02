package com.application.lamion.feature.projects.settings.controller.payload

import com.application.lamion.feature.shared.payload.dto.TokenDto
import com.fasterxml.jackson.annotation.JsonProperty

data class ProjectSettingsResponse(
    @field:JsonProperty("title")
    val title: String,

    @field:JsonProperty("description")
    val description: String?,

    @field:JsonProperty("tokens")
    val tokens: List<TokenDto>,
)