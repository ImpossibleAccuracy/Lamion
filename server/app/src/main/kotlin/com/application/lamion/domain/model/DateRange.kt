package com.application.lamion.domain.model

import com.application.lamion.utils.now
import kotlinx.datetime.LocalDateTime

data class ExtendedDateRange(
    val first: DateRange,
    val second: DateRange,
) {
    constructor(
        start: LocalDateTime,
        step: LocalDateTime,
        end: LocalDateTime = LocalDateTime.now(),
    ) : this(
        first = DateRange(start, step),
        second = DateRange(step, end),
    )

    fun toDateRange() = second
}

data class DateRange(
    val start: LocalDateTime,
    val end: LocalDateTime = LocalDateTime.now(),
)
