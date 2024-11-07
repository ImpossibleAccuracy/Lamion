package com.lamion.domain.service.event

import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain

interface EventService {
    suspend fun countTotalEvents(project: ProjectDomain, dateRange: DateRange): Long
}