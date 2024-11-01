package com.application.lamion.feature.projects.feature.controller.payload.response

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.FeatureDto
import com.fasterxml.jackson.annotation.JsonProperty

data class TopFeaturesResponse(
    @JsonProperty("items")
    val items: ChartDto<FeatureDto.Partial, Long>,

    @JsonProperty("total_events")
    val totalEvents: Long,

    @JsonProperty("avg_events_per_day")
    val avgEventsPerDay: Long,
)
