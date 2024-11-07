package com.lamion.feature.shared.mapper

import com.lamion.domain.model.CalendarItemDomain
import com.lamion.feature.shared.payload.CalendarItemDto

fun CalendarItemDomain.toDto() = CalendarItemDto(
    date = date,
    activity = activity.map { (enum, value) ->
        when (enum) {
            CalendarItemDomain.Type.USERS -> CalendarItemDto.Type.USERS
            CalendarItemDomain.Type.ERRORS -> CalendarItemDto.Type.ERRORS
            CalendarItemDomain.Type.EVENTS -> CalendarItemDto.Type.EVENTS
        } to value
    }.associate { it }
)