package com.application.lamion.feature.projects.activity.controller.payload

import com.application.lamion.feature.shared.payload.CalendarItemDto
import com.fasterxml.jackson.annotation.JsonProperty

data class ActivityResponse(
    @field:JsonProperty("calendar")
    val calendar: List<CalendarItemDto>,
)
