package com.application.lamion.server.payload.dto

import java.util.*

@DTO
data class RequestDto(
    val id: Int,
    val eventId: Int,
    val device: String,
    val date: Date,
)
