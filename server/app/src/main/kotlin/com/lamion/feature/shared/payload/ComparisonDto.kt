package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class ComparisonDto<T>(
    @JsonProperty("actual")
    val actual: T,

    @JsonProperty("past")
    val past: T,
)