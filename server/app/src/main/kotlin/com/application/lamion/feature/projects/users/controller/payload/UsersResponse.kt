package com.application.lamion.feature.projects.users.controller.payload

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.ComparisonDto
import com.application.lamion.feature.shared.payload.dto.DeviceDto
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class UsersResponse(
    @field:JsonProperty("total_users_chart")
    val totalUsersChart: ChartDto<LocalDate, Long>,

    @field:JsonProperty("active_users_chart")
    val activeUsersChart: ChartDto<LocalDate, Long>,

    @field:JsonProperty("growth_rate")
    val growthRate: ComparisonDto<Double>,

    @field:JsonProperty("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>,

    @field:JsonProperty("platforms")
    val platforms: ChartDto<String, Double>,

    @field:JsonProperty("top_devices")
    val topDevices: List<DeviceDto.Partial>,
)
