package com.lamion.feature.projects.activity.domain.service

import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.activity.ActivityService
import com.lamion.feature.projects.activity.domain.model.ActivityDetails
import kotlinx.datetime.LocalDate

interface ExtendedActivityService : ActivityService {
    suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails
}