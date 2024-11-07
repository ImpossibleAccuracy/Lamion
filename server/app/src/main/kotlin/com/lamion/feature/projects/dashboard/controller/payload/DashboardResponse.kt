package com.lamion.feature.projects.dashboard.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.CalendarItemDto
import com.lamion.feature.shared.payload.ChartDto
import com.lamion.feature.shared.payload.ComparisonDto
import com.lamion.feature.shared.payload.FeatureDto
import kotlinx.datetime.LocalTime

data class DashboardResponse(
    @JsonProperty("title")
    val title: String,

    @JsonProperty("scaling")
    val scaling: Scaling,

    @JsonProperty("top_features")
    val topFeatures: List<FeatureDto.WithEvents>,

    @JsonProperty("calendar")
    val calendar: List<CalendarItemDto>,

    @JsonProperty("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>
) {
    data class Scaling(
        @JsonProperty("total_users")
        val totalUsers: ComparisonDto<Long>,

        @JsonProperty("active_users")
        val activeUsers: ComparisonDto<Long>,

        @JsonProperty("total_crashes")
        val totalCrashes: ComparisonDto<Long>,

        @JsonProperty("triggered_events")
        val triggeredEvents: ComparisonDto<Long>,
    )
}