package com.application.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty

@JvmInline
value class ChartDto<K, T>(
    val items: List<ChartItem<K, T>>,
) {
    data class ChartItem<K, T>(
        @field:JsonProperty("key")
        val date: K,

        @field:JsonProperty("value")
        val value: T
    )
}