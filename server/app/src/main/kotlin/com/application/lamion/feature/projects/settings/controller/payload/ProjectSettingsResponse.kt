package com.application.lamion.feature.projects.settings.controller.payload

import com.application.lamion.feature.shared.payload.dto.TokenDto

data class ProjectSettingsResponse(
    val title: String,
    val description: String?,
    val tokens: List<TokenDto>,
)