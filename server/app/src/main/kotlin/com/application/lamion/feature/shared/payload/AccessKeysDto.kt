package com.application.lamion.feature.shared.payload

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessKeysDto(
    @SerialName("title")
    val title: String,

    @SerialName("created_at")
    val createdAt: LocalDate,
)