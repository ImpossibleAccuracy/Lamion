package com.lamion.feature.projects.device.domain

import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain

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