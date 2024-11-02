package com.application.lamion.feature.projects.settings.domain.model

import kotlinx.datetime.LocalDate

data class TokenDomain(
    val title: String,
    val createdAt: LocalDate,
)