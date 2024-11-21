package com.lamion.feature.projects.feature.controller.payload.response

import com.lamion.feature.shared.payload.FeatureDto
import com.lamion.feature.shared.payload.ProgressDto
import java.time.LocalDate

data class FeatureDetailsResponse(
    val feature: FeatureDto.Partial,
    val tags: List<String>,
    val events: ProgressDto<LocalDate, Long>,
    val errors: ProgressDto<LocalDate, Long>,
)
