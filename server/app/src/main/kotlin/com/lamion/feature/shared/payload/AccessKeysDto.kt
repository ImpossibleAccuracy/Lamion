package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id
import java.time.LocalDate

data class AccessKeysDto(
    @JsonProperty("id")
    val id: Id,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("created_at")
    val createdAt: LocalDate,
)

data class AccessKeysWithValueDto(
    @JsonProperty("id")
    val id: Id,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("value")
    val value: String,

    @JsonProperty("created_at")
    val createdAt: LocalDate,
)
