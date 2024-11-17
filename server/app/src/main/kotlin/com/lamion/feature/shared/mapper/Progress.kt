package com.lamion.feature.shared.mapper

import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.ComparisonDomain
import com.lamion.domain.model.DateRange
import com.lamion.feature.shared.payload.ProgressDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDateTime

fun <T> buildProgressDto(
    dateRange: DateRange,
    chart: ChartDomain<LocalDate, T>,
    comparison: ComparisonDomain<T>
) = ProgressDto(
    from = dateRange.start.toJavaLocalDateTime().toLocalDate(),
    to = dateRange.end.toJavaLocalDateTime().toLocalDate(),
    comparison = comparison.toDto(),
    chart = chart.toDateTimeDto()
)
