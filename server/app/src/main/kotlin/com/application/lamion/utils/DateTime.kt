package com.application.lamion.utils

import kotlinx.datetime.*

fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.UTC) =
    Clock.System.now().toLocalDateTime(timeZone).date

fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.UTC) =
    Clock.System.now().toLocalDateTime(timeZone)

fun LocalDate.toDateTime() =
    LocalDateTime(
        year = this.year,
        monthNumber = this.monthNumber,
        dayOfMonth = this.dayOfMonth,
        hour = 0,
        minute = 0,
    )

fun LocalDate.atStartOfDay() = atTime(0, 0, 0)

fun LocalDateTime.minus(
    period: DateTimePeriod,
) = this.date
    .minus(
        DatePeriod(
            years = period.years,
            months = period.months,
            days = period.days,
        )
    )
    .atTime(
        LocalTime(
            hour = hour - period.hours,
            minute = minute - period.minutes,
            second = second - period.seconds,
            nanosecond = nanosecond - period.nanoseconds,
        )
    )
