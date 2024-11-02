package com.application.lamion.feature.projects.activity.controller.payload

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.FeatureDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityDetailsDto(
    @SerialName("date")
    val date: LocalDate,

    @SerialName("active_users")
    val activeUsers: Long,

    @SerialName("total_events")
    val totalEvents: Long,

    @SerialName("crashes")
    val crashes: Long,

    @SerialName("top_features")
    val topFeatures: List<FeatureDto.WithEvents>,

    @SerialName("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>
)
