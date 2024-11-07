package com.lamion.domain

import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime

interface EventService {
    suspend fun logEvents(
        projectId: Id,
        userId: Id,
        deviceId: Id,
        events: List<Pair<String, LocalDateTime>>,
    )
}