package com.application.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class CalendarItemDto(
    @field:JsonProperty("date")
    val date: LocalDate,

    @field:JsonProperty("types")
    val types: List<Type>
) {
    enum class Type {
        USERS,
        ERRORS,
        EVENTS,
    }
}