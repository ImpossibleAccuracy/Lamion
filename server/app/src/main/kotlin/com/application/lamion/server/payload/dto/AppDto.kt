package com.application.lamion.server.payload.dto

import java.util.*

@DTO
data class AppDto(
    val id: Int,
    val title: String,
    val description: String?,
    val date: Date,
    val userId: Int,
)
