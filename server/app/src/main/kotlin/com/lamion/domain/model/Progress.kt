package com.lamion.domain.model

import kotlinx.datetime.LocalDate


data class Progress<K, T>(
    val from: LocalDate,
    val to: LocalDate,
    val comparison: ComparisonDomain<T>,
    val chart: ChartDomain<K, T>,
)
