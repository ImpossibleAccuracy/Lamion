package com.application.lamion.feature.shared.mapper

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.ChartItemDto

fun <K, T> ChartDomain<K, T>.toDto(): List<ChartItemDto<K, T>> =
    map {
        ChartItemDto(
            date = it.key,
            value = it.value,
        )
    }

fun <K, T, K2, T2> ChartDomain<K, T>.mapToDto(
    mapper: (K, T) -> Pair<K2, T2>,
): ChartDto<K2, T2> = map { item ->
    mapper(item.key, item.value).let {
        ChartItemDto(
            date = it.first,
            value = it.second,
        )
    }
}
