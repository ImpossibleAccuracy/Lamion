package com.application.lamion.feature.projects.feature.controller.payload.response

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.FeatureDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopFeaturesResponse(
    @SerialName("items")
    val items: ChartDto<FeatureDto.Partial, Long>,

    @SerialName("total_events")
    val totalEvents: Long,

    @SerialName("avg_events_per_day")
    val avgEventsPerDay: Long,
)
