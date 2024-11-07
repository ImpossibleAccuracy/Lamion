package com.lamion.feature.shared.payload

import com.lamion.domain.model.Id
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
