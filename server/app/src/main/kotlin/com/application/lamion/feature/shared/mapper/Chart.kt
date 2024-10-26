package com.application.lamion.feature.shared.mapper

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.feature.shared.payload.ChartDto

fun <K, T> ChartDomain<K, T>.toDto(): ChartDto<K, T> = ChartDto(
    items = items.map {
        ChartDto.ChartItem(
            date = it.date,
            value = it.value,
        )
    }
)

fun <K, T, K2, T2> ChartDomain<K, T>.mapToDto(
    mapper: (K, T) -> Pair<K2, T2>,
): ChartDto<K2, T2> = ChartDto(
    items = items.map { item ->
        mapper(item.date, item.value).let {
            ChartDto.ChartItem(
                date = it.first,
                value = it.second,
            )
        }
    }
)
