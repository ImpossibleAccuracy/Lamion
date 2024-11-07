package com.lamion.feature.projects.activity.domain.model

import kotlinx.datetime.LocalDate

data class ActivityDetails(
    val date: LocalDate,
    val activeUsers: Long,
    val totalEvents: Long,
    val crashes: Long,
)
