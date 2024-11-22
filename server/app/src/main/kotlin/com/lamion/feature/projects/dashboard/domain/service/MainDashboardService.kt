package com.lamion.feature.projects.dashboard.domain.service

import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.dashboard.domain.model.ProjectScaling

interface MainDashboardService {
    suspend fun getScaling(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
    ): ProjectScaling
}