package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate

data class AccessKeysDto(
    @JsonProperty("title")
    val title: String,

    @JsonProperty("created_at")
    val createdAt: LocalDate,
)