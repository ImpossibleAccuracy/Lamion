package com.application.lamion.feature.projects.settings.controller.payload

import com.application.lamion.feature.shared.payload.dto.AccessKeysDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectSettingsResponse(
    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String?,

    @SerialName("tokens")
    val accessKeys: List<AccessKeysDto>,
)