package com.lamion.feature.projects.feature.controller.payload.request

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateFeatureRequest(
    @SerialName("title")
    @field:NotBlank
    val title: String,

    @SerialName("description")
    @field:NotBlank
    val description: String,
)
