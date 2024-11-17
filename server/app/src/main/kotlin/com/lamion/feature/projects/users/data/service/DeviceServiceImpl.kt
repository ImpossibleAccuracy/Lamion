package com.lamion.feature.projects.users.data.service

import com.lamion.data.database.table.project.DevicePlatformTable
import com.lamion.data.database.table.project.DeviceTable
import com.lamion.domain.model.ComparisonDomain
import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.users.data.datasource.DeviceDataSource
import com.lamion.feature.projects.users.domain.model.DeviceDomain
import com.lamion.feature.projects.users.domain.service.DeviceService
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.alias
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl : DeviceService {
    companion object {
        // TODO: extract pagination
        const val DEVICES_PAGE_SIZE = 50
    }

    // FIXME: slow performance
    override suspend fun getPartialDeviceList(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
        count: Int,
    ): List<DeviceDomain.Partial> = dbQuery {
        val (currentMonthActivity, prevMonthActivity) = deviceActivityQueries(dateRange, project)

        DeviceDataSource
            .getDevicesWithActivity(
                projectId = project.id,
                currentMonthActivity = currentMonthActivity,
                prevMonthActivity = prevMonthActivity,
                count = count
            )
            .map {
                DeviceDomain.Partial(
                    title = it[DeviceTable.title],
                    activity = ComparisonDomain(
                        actual = it[currentMonthActivity]!!,
                        past = it[prevMonthActivity]!!
                    ),
                    platform = it[DevicePlatformTable.title]
                )
            }
    }

    // FIXME: slow performance
    override suspend fun getDetailedDeviceList(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
        page: Long,
    ): List<DeviceDomain.Detailed> = dbQuery {
        val (currentMonthActivity, prevMonthActivity) = deviceActivityQueries(dateRange, project)

        val currentMonthErrors = DeviceDataSource.createErrorSubquery(
            projectId = project.id,
            start = dateRange.second.start,
            end = dateRange.second.end,
        ).alias("startErrors")

        val prevMonthErrors = DeviceDataSource.createErrorSubquery(
            projectId = project.id,
            start = dateRange.first.start,
            end = dateRange.first.end,
        ).alias("endErrors")

        DeviceDataSource
            .getDevicesWithActivityAndErrors(
                projectId = project.id,
                currentMonthActivity = currentMonthActivity,
                prevMonthActivity = prevMonthActivity,
                currentMonthErrors = currentMonthErrors,
                prevMonthErrors = prevMonthErrors,
                limit = DEVICES_PAGE_SIZE,
                offset = page * DEVICES_PAGE_SIZE
            )
            .map {
                DeviceDomain.Detailed(
                    title = it[DeviceTable.title],
                    activity = ComparisonDomain(
                        actual = it[currentMonthActivity]!!,
                        past = it[prevMonthActivity]!!
                    ),
                    platform = it[DevicePlatformTable.title],
                    errors = ComparisonDomain(
                        actual = it[currentMonthErrors]!!,
                        past = it[prevMonthErrors]!!
                    ),
                )
            }
    }

    private fun deviceActivityQueries(
        dateRange: ExtendedDateRange,
        project: ProjectDomain
    ): Pair<Expression<Long?>, Expression<Long?>> {
        val currentMonthActivity = DeviceDataSource.createActivitySubquery(
            projectId = project.id,
            start = dateRange.second.start,
            end = dateRange.second.end,
        ).alias("startActivity")

        val prevMonthActivity = DeviceDataSource.createActivitySubquery(
            projectId = project.id,
            start = dateRange.first.start,
            end = dateRange.first.end,
        ).alias("endActivity")

        return Pair(currentMonthActivity, prevMonthActivity)
    }
}