package com.application.lamion.feature.projects.users.controller.payload

import com.application.lamion.feature.shared.payload.ChartDto
import com.application.lamion.feature.shared.payload.dto.ComparisonDto
import com.application.lamion.feature.shared.payload.dto.DeviceDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalTime

data class UsersResponse(
    @JsonProperty("total_users_chart")
    val totalUsersChart: ChartDto<LocalDate, Long>,

    @JsonProperty("active_users_chart")
    val activeUsersChart: ChartDto<LocalDate, Long>,

    @JsonProperty("growth_rate")
    val growthRate: ComparisonDto<Double>,

    @JsonProperty("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>,

    @JsonProperty("platforms")
    val platforms: ChartDto<String, Double>,

    @JsonProperty("top_devices")
    val topDevices: List<DeviceDto.Partial>,
)
