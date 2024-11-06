package com.application.lamion.feature.projects.feature.domain.service

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import kotlinx.datetime.LocalDate

interface FeatureDashboardService {
    suspend fun countAverageEventsPerDay(project: ProjectDomain, dateRange: DateRange): Double

    suspend fun countEventsGroupByDate(project: ProjectDomain, dateRange: DateRange): ChartDomain<LocalDate, Long>

    suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int
    ): ChartDomain<FeatureDomain.Partial, Long>
}