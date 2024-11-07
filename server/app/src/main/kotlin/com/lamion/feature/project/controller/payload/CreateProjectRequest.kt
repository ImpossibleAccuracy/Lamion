package com.lamion.feature.project.controller.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectRequest(
    @field:NotNull
    @field:NotBlank
    @SerialName("title")
    var title: String,

    @SerialName("description")
    var description: String?,
)
