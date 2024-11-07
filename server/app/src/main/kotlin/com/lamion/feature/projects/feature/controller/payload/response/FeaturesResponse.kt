package com.lamion.feature.projects.feature.controller.payload.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.ChartDto
import kotlinx.datetime.LocalDate

data class FeaturesResponse(
    @JsonProperty("total_events")
    val events: ChartDto<LocalDate, Long>,

    @JsonProperty("total_features")
    val totalFeatures: Long,
)
