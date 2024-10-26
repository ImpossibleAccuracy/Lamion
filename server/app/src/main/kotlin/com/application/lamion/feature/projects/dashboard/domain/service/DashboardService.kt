package com.application.lamion.feature.projects.dashboard.domain.service

import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.dashboard.domain.model.ProjectScaling

interface DashboardService {
    suspend fun getScaling(project: ProjectDomain): ProjectScaling

    suspend fun getTopFeatures(project: ProjectDomain): List<FeatureWithEvents>
}