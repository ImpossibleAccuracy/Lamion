package com.application.lamion.domain.service

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

interface ActivityService {
    suspend fun getProjectActivity(
        project: ProjectDomain,
        start: LocalDateTime,
        end: LocalDateTime?,
    ): List<CalendarItemDomain>

    suspend fun getUserActivityTime(
        project: ProjectDomain,
        start: LocalDate,
        end: LocalDate?
    ): ChartDomain<LocalTime, Long>

    suspend fun getTopFeatures(
        project: ProjectDomain,
        start: LocalDateTime,
        end: LocalDateTime?,
        count: Int,
    ): List<FeatureWithEvents>
}