package com.application.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class CalendarItemDto(
    @JsonProperty("date")
    val date: LocalDate,

    @JsonProperty("types")
    val activity: Map<Type, Long>
) {
    enum class Type {
        USERS,
        ERRORS,
        EVENTS,
    }
}