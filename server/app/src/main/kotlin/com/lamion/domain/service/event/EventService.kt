package com.lamion.domain.service.event

import com.lamion.domain.model.*
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import kotlinx.datetime.LocalDate

interface EventService {
    suspend fun getEventsComparison(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun getEventsComparison(
        feature: FeatureDomain,
        dateRange: ExtendedDateRange,
    ): ComparisonDomain<Long>

    suspend fun countTotalEvents(
        project: ProjectDomain,
        dateRange: DateRange
    ): Long

    suspend fun countAverageEventsPerDay(
        project: ProjectDomain,
        dateRange: DateRange
    ): Double

    suspend fun countEventsGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long>

    suspend fun countEventsGroupByDate(
        feature: FeatureDomain,
        dateRange: DateRange,
    ): ChartDomain<LocalDate, Long>

    suspend fun countEventsByPlatform(
        project: ProjectDomain,
        dateRange: DateRange,
    ): ChartDomain<String, Double>
}