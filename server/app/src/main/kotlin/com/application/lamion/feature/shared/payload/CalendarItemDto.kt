package com.application.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.datetime.LocalDate

data class CalendarItemDto(
    @field:JsonProperty("date")
    val date: LocalDate,

    @field:JsonProperty("types")
    val activity: Map<Type, Long>
) {
    enum class Type {
        USERS,
        ERRORS,
        EVENTS,
    }
}