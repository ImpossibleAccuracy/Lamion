package com.lamion.feature.projects.settings.controller.payload

import com.lamion.feature.shared.payload.AccessKeysDto
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