package com.application.lamion.domain.model

import com.application.lamion.utils.now
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.temporal.ChronoUnit

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
) {
    val totalDays by lazy {
        if (start.date == end.date) return@lazy 1

        start.toJavaLocalDateTime()
            .until(end.toJavaLocalDateTime(), ChronoUnit.DAYS)
    }
}
