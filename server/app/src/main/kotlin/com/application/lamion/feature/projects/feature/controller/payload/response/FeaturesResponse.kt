package com.application.lamion.feature.projects.feature.controller.payload.response

import com.application.lamion.feature.shared.payload.ChartDto
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate

data class FeaturesResponse(
    @field:JsonProperty("total_events")
    val totalEvents: ChartDto<LocalDate, Long>,

    @field:JsonProperty("total_features")
    val totalFeatures: Long,
)
