package com.lamion.domain.service.activity

import com.lamion.domain.model.CalendarItemDomain
import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain
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
}