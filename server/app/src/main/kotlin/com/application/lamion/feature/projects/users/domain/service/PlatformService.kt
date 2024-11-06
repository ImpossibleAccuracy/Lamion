package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain

interface PlatformService {
    suspend fun countEventsGroupByPlatforms(project: ProjectDomain, dateRange: DateRange): ChartDomain<String, Double>
}