package com.application.lamion.domain.model

import com.application.lamion.utils.atStartOfDay
import com.application.lamion.utils.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

enum class TimePeriod {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    companion object {
        val DEFAULT = MONTH
    }

    fun toExtendedDateRange() =
        ExtendedDateRange(
            start = toLocalDate(multiplier = 2).atStartOfDay(),
            step = toLocalDate().atStartOfDay(),
        )

    fun toSimpleDateRange() =
        DateRange(
            start = toLocalDate().atStartOfDay(),
        )

    fun toLocalDate(base: LocalDate = LocalDate.now(), multiplier: Int = 1): LocalDate =
        base.minus(
            when (this) {
                DAY -> DatePeriod(days = 1)
                WEEK -> DatePeriod(days = 7 * multiplier)
                MONTH -> DatePeriod(months = 1 * multiplier)
                YEAR -> DatePeriod(years = 1 * multiplier)
            }
        )
}