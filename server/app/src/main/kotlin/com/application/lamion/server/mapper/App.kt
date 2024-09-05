package com.application.lamion.server.mapper

import com.application.lamion.domain.model.AppAnalyticsDomain
import com.application.lamion.domain.model.AppDomain
import com.application.lamion.server.payload.dto.AppAnalyticsDto
import com.application.lamion.server.payload.dto.AppDto

fun AppDomain.toDto() = AppDto(
    id = id,
    title = title,
    description = description,
    date = date,
    userId = userId
)

fun AppAnalyticsDomain.toDto() = AppAnalyticsDto(
    id = id,
    title = title,
    description = description,
    date = date,
    userId = userId,
    eventsCount = eventsCount,
    requestsCount = requestsCount
)