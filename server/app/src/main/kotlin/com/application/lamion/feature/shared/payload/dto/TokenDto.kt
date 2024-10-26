package com.application.lamion.feature.shared.payload.dto

import java.time.LocalDate

data class TokenDto(
    val title: String,
    val createdAt: LocalDate,
)