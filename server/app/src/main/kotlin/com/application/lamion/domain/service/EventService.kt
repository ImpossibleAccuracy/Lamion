package com.application.lamion.domain.service

import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain

interface EventService {
    suspend fun countTotalEvents(project: ProjectDomain, dateRange: DateRange): Long
}