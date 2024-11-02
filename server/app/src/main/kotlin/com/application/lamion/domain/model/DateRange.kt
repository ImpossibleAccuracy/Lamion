package com.application.lamion.domain.model

import com.application.lamion.utils.now
import kotlinx.datetime.LocalDateTime

data class DateRange(
    val firstStart: LocalDateTime,
    val firstEnd: LocalDateTime,

    val finishStart: LocalDateTime,
    val finishEnd: LocalDateTime,
) {
    constructor(
        start: LocalDateTime,
        step: LocalDateTime,
        end: LocalDateTime = LocalDateTime.now(),
    ) : this(
        firstStart = start,
        firstEnd = step,
        finishStart = step,
        finishEnd = end,
    )
}
