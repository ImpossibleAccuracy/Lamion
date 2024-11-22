package com.lamion.domain.model

import kotlinx.datetime.LocalDateTime

data class IncomingEvent(
    val function: String,
    val feature: String?,
    val createdAt: LocalDateTime,
)
