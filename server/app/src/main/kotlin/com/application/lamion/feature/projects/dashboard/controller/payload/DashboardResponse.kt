package com.application.lamion.feature.projects.dashboard.controller.payload

import com.application.lamion.feature.shared.payload.CalendarItemDto
import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.ComparisonDto
import com.application.lamion.feature.shared.payload.dto.FeatureDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime

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

        @JsonProperty("active_uUsers")
        val activeUsers: ComparisonDto<Long>,

        @JsonProperty("total_crashes")
        val totalCrashes: ComparisonDto<Long>,

        @JsonProperty("triggeredEvents")
        val triggeredEvents: ComparisonDto<Long>,
    )
}