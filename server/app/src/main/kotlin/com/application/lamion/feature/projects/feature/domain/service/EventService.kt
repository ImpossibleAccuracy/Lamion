package com.application.lamion.feature.projects.feature.domain.service

import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain

interface EventService {
    suspend fun getEventsCount(project: ProjectDomain, dateRange: DateRange): Long

    suspend fun getAverageEventsPerDay(project: ProjectDomain, dateRange: DateRange): Double
}