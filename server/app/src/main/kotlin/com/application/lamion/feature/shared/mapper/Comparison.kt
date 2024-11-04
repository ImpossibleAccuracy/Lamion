package com.application.lamion.feature.shared.mapper

import com.application.lamion.domain.model.ComparisonDomain
import com.application.lamion.feature.shared.payload.ComparisonDto

fun <T> ComparisonDomain<T>.toDto() = ComparisonDto(
    actual = actual,
    past = past,
)