package com.application.lamion.feature.shared.payload

import com.application.lamion.domain.model.Id
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    @SerialName("id")
    val id: Id,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String?,
)
