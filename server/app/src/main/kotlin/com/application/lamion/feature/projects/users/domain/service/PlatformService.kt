package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.ProjectDomain

interface PlatformService {
    suspend fun getPlatforms(project: ProjectDomain): ChartDomain<String, Double>
}