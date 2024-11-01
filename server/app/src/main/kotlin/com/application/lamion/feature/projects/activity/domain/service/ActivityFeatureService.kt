package com.application.lamion.feature.projects.activity.domain.service

import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import java.time.LocalDate

interface ActivityFeatureService : ActivityService {
    suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails

    suspend fun getTopFeatures(date: LocalDate, project: ProjectDomain): List<FeatureWithEvents>
}