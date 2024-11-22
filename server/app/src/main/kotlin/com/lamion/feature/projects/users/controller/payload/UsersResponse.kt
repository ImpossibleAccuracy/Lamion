package com.lamion.feature.projects.users.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.ChartDto
import com.lamion.feature.shared.payload.ComparisonDto
import com.lamion.feature.shared.payload.DeviceDto
import com.lamion.feature.shared.payload.ProgressDto
import java.time.LocalDate
import java.time.LocalTime

data class UsersResponse(
    @JsonProperty("total_users")
    val totalUsers: ProgressDto<LocalDate, Long>,

    @JsonProperty("active_users")
    val activeUsers: ProgressDto<LocalDate, Long>,

    @JsonProperty("growth_rate")
    val growthRate: ComparisonDto<Double>,

    @JsonProperty("user_activity_time")
    val userActivityTime: ChartDto<LocalTime, Long>,

    @JsonProperty("platforms")
    val platforms: ChartDto<String, Double>,

    @JsonProperty("top_devices")
    val topDevices: List<DeviceDto.Partial>,
)
