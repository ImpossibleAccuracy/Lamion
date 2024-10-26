package com.application.lamion.domain.model

import java.time.LocalDate

data class CalendarItemDomain(
    val date: LocalDate,
    val types: List<Type>
) {
    enum class Type {
        USERS,
        ERRORS,
        EVENTS,
    }
}