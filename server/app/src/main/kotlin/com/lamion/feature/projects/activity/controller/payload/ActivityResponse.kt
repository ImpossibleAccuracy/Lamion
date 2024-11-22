package com.lamion.feature.projects.activity.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.CalendarItemDto

data class ActivityResponse(
    @JsonProperty("calendar")
    val calendar: List<CalendarItemDto>,
)
