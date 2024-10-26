package com.application.lamion.feature.projects.feature.controller.payload.response

import com.application.lamion.feature.shared.payload.ChartDto
import java.time.LocalDate

data class FeaturesResponse(
    val totalEvents: ChartDto<LocalDate, Long>,
    val totalFeatures: Long,
)
