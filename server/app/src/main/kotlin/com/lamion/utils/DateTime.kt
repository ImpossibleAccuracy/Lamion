package com.lamion.utils

import kotlinx.datetime.*

fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.UTC) =
    Clock.System.now().toLocalDateTime(timeZone).date

fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.UTC) =
    Clock.System.now().toLocalDateTime(timeZone)

fun LocalDate.atStartOfDay() = atTime(0, 0, 0)
