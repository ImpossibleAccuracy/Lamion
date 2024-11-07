package com.lamion.feature.shared.mapper

import com.lamion.domain.model.ComparisonDomain
import com.lamion.feature.shared.payload.ComparisonDto

fun <T> ComparisonDomain<T>.toDto() = ComparisonDto(
    actual = actual,
    past = past,
)