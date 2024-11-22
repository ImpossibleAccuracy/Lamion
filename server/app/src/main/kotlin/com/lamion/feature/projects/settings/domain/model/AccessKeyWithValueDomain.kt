package com.lamion.feature.projects.settings.domain.model

import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDate

data class AccessKeyWithValueDomain(
    val id: Id,
    val title: String,
    val value: String,
    val createdAt: LocalDate,
)