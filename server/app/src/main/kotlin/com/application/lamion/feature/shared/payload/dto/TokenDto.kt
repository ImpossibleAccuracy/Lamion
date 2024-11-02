package com.application.lamion.feature.shared.payload.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    @SerialName("title")
    val title: String,

    @SerialName("created_at")
    val createdAt: LocalDate,
)