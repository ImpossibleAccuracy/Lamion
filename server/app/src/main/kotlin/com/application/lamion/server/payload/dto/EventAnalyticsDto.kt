package com.application.lamion.server.payload.dto

import java.util.*

@DTO
data class EventAnalyticsDto(
    val id: Int,
    val title: String,
    val date: Date,
    val applicationId: Int,
    val requestsCount: Int,
    val mostUsedDevice: String,
    val highDemandTime: String,
)
