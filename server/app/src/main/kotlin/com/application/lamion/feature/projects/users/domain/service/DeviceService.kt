package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.ExtendedDateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.users.domain.model.DeviceDomain

interface DeviceService {
    suspend fun getPartialDeviceList(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
        count: Int,
    ): List<DeviceDomain.Partial>

    suspend fun getDetailedDeviceList(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
        page: Long,
    ): List<DeviceDomain.Detailed>
}