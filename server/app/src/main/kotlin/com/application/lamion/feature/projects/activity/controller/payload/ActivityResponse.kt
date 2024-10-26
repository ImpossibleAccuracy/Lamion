package com.application.lamion.feature.projects.activity.controller.payload

import com.application.lamion.feature.shared.payload.CalendarItemDto

data class ActivityResponse(
    val calendar: List<CalendarItemDto>,
)
