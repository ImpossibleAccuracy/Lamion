package com.application.lamion.domain.service

import com.application.lamion.domain.model.*
import kotlinx.datetime.LocalTime

interface ActivityService {
    suspend fun getProjectActivity(
        project: ProjectDomain,
        dateRange: DateRange,
    ): List<CalendarItemDomain>

    suspend fun getUserActivityTime(
        project: ProjectDomain,
        dateRange: DateRange,
    ): ChartDomain<LocalTime, Long>

    suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int,
    ): List<FeatureWithEvents>
}