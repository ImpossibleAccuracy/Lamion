package com.application.lamion.server.payload.dto

import java.util.*

@DTO
data class EventDto(
    val id: Int,
    val title: String,
    val date: Date,
    val applicationId: Int,
)
