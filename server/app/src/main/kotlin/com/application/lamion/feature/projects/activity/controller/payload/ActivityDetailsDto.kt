package com.application.lamion.feature.projects.activity.controller.payload

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.FeatureDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class ActivityDetailsDto(
    val date: LocalDate,
    val activeUsers: Long,
    val totalEvents: Long,
    val crashes: Long,
    val topFeatures: List<FeatureDto.WithEvents>,
    val userActivityTime: ChartDto<LocalTime, Long>
)
