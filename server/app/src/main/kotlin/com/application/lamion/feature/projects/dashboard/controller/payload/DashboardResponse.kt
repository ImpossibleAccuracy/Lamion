package com.application.lamion.feature.projects.dashboard.controller.payload

import com.application.lamion.feature.shared.payload.CalendarItemDto
import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.ComparisonDto
import com.application.lamion.feature.shared.payload.dto.FeatureDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime

data class DashboardResponse(
    @field:JsonProperty("title")
    val title: String,

    @field:JsonProperty("scaling")
    val scaling: Scaling,

    @field:JsonProperty("top_features")
    val topFeatures: List<FeatureDto.WithEvents>,

    @field:JsonProperty("calendar")
    val calendar: List<CalendarItemDto>,

    @field:JsonProperty("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>
) {
    data class Scaling(
        @field:JsonProperty("total_users")
        val totalUsers: ComparisonDto<Long>,

        @field:JsonProperty("active_uUsers")
        val activeUsers: ComparisonDto<Long>,

        @field:JsonProperty("total_crashes")
        val totalCrashes: ComparisonDto<Long>,

        @field:JsonProperty("triggeredEvents")
        val triggeredEvents: ComparisonDto<Long>,
    )
}