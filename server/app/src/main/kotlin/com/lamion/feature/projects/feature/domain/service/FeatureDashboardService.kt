package com.lamion.feature.projects.feature.domain.service

import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
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