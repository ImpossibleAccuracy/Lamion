package com.application.lamion.feature.projects.users.data

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.users.domain.service.PlatformService
import org.springframework.stereotype.Service

@Service
class PlatformServiceImpl : PlatformService {
    override suspend fun getPlatforms(project: ProjectDomain): ChartDomain<String, Double> {
        /*
        Query to deviceplatform
        with join to projectdevice
        and join to events
        and join to functions
        and check project_io
        */

        TODO("Not yet implemented")
    }
}