package com.application.lamion.feature.projects.activity.domain.service

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import java.time.LocalDate

interface ActivityFeatureService : ActivityService {
    suspend fun getActivity(project: ProjectDomain, month: LocalDate): List<CalendarItemDomain>

    suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails

    suspend fun getTopFeatures(date: LocalDate, project: ProjectDomain): List<FeatureWithEvents>

    suspend fun getUserActivityTime(date: LocalDate, project: ProjectDomain): ChartDomain<LocalDate, Long>
}