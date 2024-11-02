package com.application.lamion.domain.model

import com.application.lamion.utils.now
import com.application.lamion.utils.toDateTime
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

    fun toLocalDate(base: LocalDate = LocalDate.now(), multiplier: Int = 1): LocalDate =
        base.minus(
            when (this) {
                DAY -> DatePeriod(days = 1 * multiplier)
                WEEK -> DatePeriod(days = 7 * multiplier)
                MONTH -> DatePeriod(months = 1 * multiplier)
                YEAR -> DatePeriod(years = 1 * multiplier)
            }
        )

    fun toDateRange() =
        DateRange(
            start = toLocalDate(multiplier = 2).toDateTime(),
            step = toLocalDate().toDateTime(),
        )
}