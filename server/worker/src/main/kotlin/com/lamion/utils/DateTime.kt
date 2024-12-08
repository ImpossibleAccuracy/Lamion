package com.lamion.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.Companion.fromEpochMilliseconds(millis: Long) =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC)
