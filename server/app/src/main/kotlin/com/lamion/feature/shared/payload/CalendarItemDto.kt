package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate

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