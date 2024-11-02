package com.application.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty

typealias ChartDto<K, T> = List<ChartItemDto<K, T>>

data class ChartItemDto<K, T>(
    @field:JsonProperty("key")
    val date: K,

    @field:JsonProperty("value")
    val value: T
)
