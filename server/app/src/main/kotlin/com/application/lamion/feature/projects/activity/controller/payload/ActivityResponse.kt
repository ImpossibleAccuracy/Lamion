package com.application.lamion.feature.projects.activity.controller.payload

import com.application.lamion.feature.shared.payload.CalendarItemDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    @SerialName("calendar")
    val calendar: List<CalendarItemDto>,
)
