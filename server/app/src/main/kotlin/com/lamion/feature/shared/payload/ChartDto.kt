package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty

typealias ChartDto<K, T> = List<ChartItemDto<K, T>>

data class ChartItemDto<K, T>(
    @JsonProperty("key")
    val date: K,

    @JsonProperty("value")
    val value: T
)
