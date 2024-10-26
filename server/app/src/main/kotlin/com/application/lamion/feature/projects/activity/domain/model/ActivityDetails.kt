package com.application.lamion.feature.projects.activity.domain.model

import java.time.LocalDate

data class ActivityDetails(
    val date: LocalDate,
    val activeUsers: Long,
    val totalEvents: Long,
    val crashes: Long,
)
