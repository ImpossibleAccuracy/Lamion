package com.application.lamion.server.mapper

import com.application.lamion.domain.model.EventAnalyticsDomain
import com.application.lamion.domain.model.EventDomain
import com.application.lamion.server.payload.dto.EventAnalyticsDto
import com.application.lamion.server.payload.dto.EventDto

fun EventDomain.toDto() = EventDto(
    id = id,
    title = title,
    date = date,
    applicationId = applicationId
)

fun EventAnalyticsDomain.toDto() = EventAnalyticsDto(
    id = id,
    title = title,
    date = date,
    applicationId = applicationId,
    requestsCount = requestsCount,
    mostUsedDevice = mostUsedDevice,
    highDemandTime = highDemandTime
)