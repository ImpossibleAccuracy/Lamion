package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.users.domain.model.DeviceDomain

interface DeviceService {
    suspend fun getTopDevices(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int,
    ): List<DeviceDomain.Partial>

    suspend fun getDevices(
        project: ProjectDomain,
        dateRange: DateRange,
        page: Long,
    ): List<DeviceDomain.Detailed>
}