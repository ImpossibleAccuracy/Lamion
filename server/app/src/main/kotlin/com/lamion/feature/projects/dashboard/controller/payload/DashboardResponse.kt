package com.lamion.feature.projects.dashboard.controller.payload

import com.lamion.feature.shared.payload.CalendarItemDto
import com.lamion.feature.shared.payload.ChartDto
import com.lamion.feature.shared.payload.ComparisonDto
import com.lamion.feature.shared.payload.FeatureDto
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardResponse(
    @SerialName("title")
    val title: String,

    @SerialName("scaling")
    val scaling: Scaling,

    @SerialName("top_features")
    val topFeatures: List<FeatureDto.WithEvents>,

    @SerialName("calendar")
    val calendar: List<CalendarItemDto>,

    @SerialName("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>
) {
    @Serializable
    data class Scaling(
        @SerialName("total_users")
        val totalUsers: ComparisonDto<Long>,

        @SerialName("active_users")
        val activeUsers: ComparisonDto<Long>,

        @SerialName("total_crashes")
        val totalCrashes: ComparisonDto<Long>,

        @SerialName("triggered_events")
        val triggeredEvents: ComparisonDto<Long>,
    )
}