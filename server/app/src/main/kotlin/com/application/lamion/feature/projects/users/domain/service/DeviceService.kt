package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.feature.projects.users.domain.model.DeviceDomain

interface DeviceService {
    suspend fun getTopDevices(project: ProjectDomain): List<DeviceDomain.Partial>

    suspend fun getDevices(
        project: ProjectDomain,
        period: TimePeriod,
        page: Long
    ): List<DeviceDomain.Detailed>
}