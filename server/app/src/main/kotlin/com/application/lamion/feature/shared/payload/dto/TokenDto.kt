package com.application.lamion.feature.shared.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate

data class TokenDto(
    @field:JsonProperty("title")
    val title: String,

    @field:JsonProperty("created_at")
    val createdAt: LocalDate,
)