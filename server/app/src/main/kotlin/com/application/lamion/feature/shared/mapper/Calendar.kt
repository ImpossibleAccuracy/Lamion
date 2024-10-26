package com.application.lamion.feature.shared.mapper

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.feature.shared.payload.CalendarItemDto

fun CalendarItemDomain.toDto() = CalendarItemDto(
    date = date,
    types = types.map {
        when (it) {
            CalendarItemDomain.Type.USERS -> CalendarItemDto.Type.USERS
            CalendarItemDomain.Type.ERRORS -> CalendarItemDto.Type.ERRORS
            CalendarItemDomain.Type.EVENTS -> CalendarItemDto.Type.EVENTS
        }
    }
)