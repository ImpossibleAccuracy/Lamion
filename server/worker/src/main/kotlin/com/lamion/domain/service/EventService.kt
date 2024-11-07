package com.lamion.domain.service

import com.lamion.domain.model.Id
import com.lamion.domain.model.IncomingEvent

interface EventService {
    suspend fun logEvents(
        projectId: Id,
        userId: Id,
        deviceId: Id,
        events: List<IncomingEvent>,
    )
}