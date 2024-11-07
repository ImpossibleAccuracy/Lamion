package com.lamion.feature.shared.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ChartDto<K, T> = List<ChartItemDto<K, T>>

@Serializable
data class ChartItemDto<K, T>(
    @SerialName("key")
    val date: K,

    @SerialName("value")
    val value: T
)
