package com.lamion.feature.project.controller.payload

import com.lamion.server.validation.NullOrNotBlank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProjectRequest(
    @field:NullOrNotBlank
    @SerialName("title")
    var title: String? = null,

    @SerialName("description")
    var description: String? = null,
)
