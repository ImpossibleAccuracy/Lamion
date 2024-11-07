package com.lamion.domain.model

import kotlinx.datetime.LocalDateTime

data class IncomingError(
    val function: String?,
    val createdAt: LocalDateTime,
    val text: String,
)
