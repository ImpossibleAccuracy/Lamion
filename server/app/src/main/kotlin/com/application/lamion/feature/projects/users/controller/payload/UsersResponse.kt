package com.application.lamion.feature.projects.users.controller.payload

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.ComparisonDto
import com.application.lamion.feature.shared.payload.DeviceDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    @SerialName("total_users_chart")
    val totalUsersChart: ChartDto<LocalDate, Long>,

    @SerialName("active_users_chart")
    val activeUsersChart: ChartDto<LocalDate, Long>,

    @SerialName("growth_rate")
    val growthRate: ComparisonDto<Double>,

    @SerialName("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>,

    @SerialName("platforms")
    val platforms: ChartDto<String, Double>,

    @SerialName("top_devices")
    val topDevices: List<DeviceDto.Partial>,
)
