package com.application.lamion.server.payload.dto

import java.util.*

@DTO
data class AppAnalyticsDto(
    val id: Int,
    val title: String,
    val description: String,
    val date: Date,
    val userId: Int,
    val eventsCount: Long,
    val requestsCount: Long,
)
