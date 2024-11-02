package com.application.lamion.feature.projects.activity.domain.service

import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import kotlinx.datetime.LocalDate

interface ActivityFeatureService : ActivityService {
    suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails
}