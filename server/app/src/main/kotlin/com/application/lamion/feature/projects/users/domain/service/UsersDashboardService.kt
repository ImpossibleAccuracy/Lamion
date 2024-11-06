package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.*
import kotlinx.datetime.LocalDate

interface UsersDashboardService {
    suspend fun countUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long>

    suspend fun countActiveUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long>

    suspend fun computeGrowthRate(
        project: ProjectDomain,
        timePeriod: TimePeriod,
    ): ComparisonDomain<Double>
}