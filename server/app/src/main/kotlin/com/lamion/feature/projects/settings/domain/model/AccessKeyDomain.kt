package com.lamion.feature.projects.settings.domain.model

import kotlinx.datetime.LocalDate

data class AccessKeyDomain(
    val title: String,
    val createdAt: LocalDate,
)