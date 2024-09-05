package com.application.lamion.server.mapper

import com.application.lamion.domain.model.RequestAnalyticsDomain
import com.application.lamion.domain.model.RequestDomain
import com.application.lamion.server.payload.dto.RequestAnalyticsDto
import com.application.lamion.server.payload.dto.RequestDto

fun RequestDomain.toDto() = RequestDto(
    id = id,
    eventId = eventId,
    device = device,
    date = date
)

fun RequestAnalyticsDomain.toDto() = RequestAnalyticsDto(
    id = id,
    device = device,
    count = count,
    eventId = eventId
)