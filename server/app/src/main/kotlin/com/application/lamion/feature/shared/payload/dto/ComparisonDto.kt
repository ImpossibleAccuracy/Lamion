package com.application.lamion.feature.shared.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ComparisonDto<T>(
    @field:JsonProperty("actual")
    val actual: T,

    @field:JsonProperty("past")
    val past: T,
)