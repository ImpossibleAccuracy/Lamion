package com.application.lamion.domain.service

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.model.ChartDomain
import java.time.LocalDate
import java.time.LocalTime

interface ActivityService {
    suspend fun getProjectActivity(project: ProjectDomain): List<CalendarItemDomain>

    suspend fun getProjectUserActivityTime(project: ProjectDomain): ChartDomain<LocalTime, Long>
}