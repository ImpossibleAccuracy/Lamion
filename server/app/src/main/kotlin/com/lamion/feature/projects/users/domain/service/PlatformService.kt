package com.lamion.feature.projects.users.domain.service

import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain

interface PlatformService {
    suspend fun countEventsGroupByPlatforms(project: ProjectDomain, dateRange: DateRange): ChartDomain<String, Double>
}