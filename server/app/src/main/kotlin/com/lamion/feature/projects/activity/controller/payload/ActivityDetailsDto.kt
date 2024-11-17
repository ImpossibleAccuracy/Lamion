package com.lamion.feature.projects.activity.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.ChartDto
import com.lamion.feature.shared.payload.FeatureDto
import java.time.LocalDate
import java.time.LocalTime

data class ActivityDetailsDto(
    @JsonProperty("date")
    val date: LocalDate,

    @JsonProperty("active_users")
    val activeUsers: Long,

    @JsonProperty("total_events")
    val totalEvents: Long,

    @JsonProperty("crashes")
    val crashes: Long,

    @JsonProperty("top_features")
    val topFeatures: List<FeatureDto.WithEvents>,

    @JsonProperty("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>
)
