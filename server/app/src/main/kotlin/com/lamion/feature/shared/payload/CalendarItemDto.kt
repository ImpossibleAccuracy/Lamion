package com.lamion.feature.shared.payload

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarItemDto(
    @SerialName("date")
    val date: LocalDate,

    @SerialName("types")
    val activity: Map<Type, Long>
) {
    enum class Type {
        USERS,
        ERRORS,
        EVENTS,
    }
}