package com.application.lamion.domain.model

import java.time.LocalDate

data class CalendarItemDomain(
    val date: LocalDate,
    val activity: Map<Type, Long>
) {
    enum class Type {
        USERS,
        ERRORS,
        EVENTS,
    }
}