package com.application.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class ChartDto<K, T>(
    val items: List<ChartItem<K, T>>,
) {
    data class ChartItem<K, T>(
        @JsonProperty("key")
        val date: K,

        @JsonProperty("value")
        val value: T
    )
}