package com.application.lamion.domain.model

@JvmInline
value class ChartDomain<K, T>(
    val items: List<ChartItem<K, T>>,
) {
    data class ChartItem<K, T>(
        val date: K,
        val value: T
    )
}