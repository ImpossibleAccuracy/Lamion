package com.application.lamion.domain.service

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.ProjectDomain
import java.time.LocalDate
import java.time.LocalTime

interface ActivityService {
    suspend fun getProjectActivity(project: ProjectDomain, month: LocalDate): List<CalendarItemDomain>

    suspend fun getUserActivityTime(date: LocalDate, project: ProjectDomain): ChartDomain<LocalTime, Long>
}