package com.lamion.feature.projects.feature.controller.payload.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.ChartDto
import com.lamion.feature.shared.payload.FeatureDto

data class TopFeaturesResponse(
    @JsonProperty("items")
    val items: ChartDto<FeatureDto.Partial, Long>,

    @JsonProperty("total_events")
    val totalEvents: Long,

    @JsonProperty("avg_events_per_day")
    val avgEventsPerDay: Long,
)
