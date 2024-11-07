package com.lamion.feature.projects.feature.controller.payload.request

import com.lamion.domain.model.Id
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateFeatureRequest(
    @SerialName("title")
    @field:NotBlank
    val title: String,

    @SerialName("description")
    @field:NotBlank
    val description: String,

    @SerialName("functions")
    val functions: List<Id>,
)
