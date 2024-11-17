package com.lamion.feature.shared.mapper

import com.lamion.domain.model.ChartDomain
import com.lamion.feature.shared.payload.ChartDto
import com.lamion.feature.shared.payload.ChartItemDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime

fun <K, T> ChartDomain<K, T>.toDto(): List<ChartItemDto<K, T>> =
    map {
        ChartItemDto(
            date = it.key,
            value = it.value,
        )
    }

@JvmName("toJDateDto")
fun <T> ChartDomain<LocalDate, T>.toDateTimeDto(): List<ChartItemDto<java.time.LocalDate, T>> =
    map {
        ChartItemDto(
            date = it.key.toJavaLocalDate(),
            value = it.value,
        )
    }

@JvmName("toJTimeDto")
fun <T> ChartDomain<LocalTime, T>.toDateTimeDto(): List<ChartItemDto<java.time.LocalTime, T>> =
    map {
        ChartItemDto(
            date = it.key.toJavaLocalTime(),
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
