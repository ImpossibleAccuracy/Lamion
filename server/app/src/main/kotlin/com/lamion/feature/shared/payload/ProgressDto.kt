package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class ProgressDto<K, T>(
    @JsonProperty("from")
    val from: LocalDate,
    @JsonProperty("to")
    val to: LocalDate,
    @JsonProperty("comparison")
    val comparison: ComparisonDto<T>,
    @JsonProperty("chart")
    val chart: ChartDto<K, T>,
)
