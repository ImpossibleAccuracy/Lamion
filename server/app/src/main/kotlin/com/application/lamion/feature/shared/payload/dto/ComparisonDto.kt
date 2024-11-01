package com.application.lamion.feature.shared.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ComparisonDto<T>(
    @JsonProperty("actual")
    val actual: T,

    @JsonProperty("past")
    val past: T,
)