package com.application.lamion.server.payload.dto

@DTO
data class RequestAnalyticsDto(
    val id: Int,
    val device: String,
    val count: Int,
    val eventId: Int,
)
