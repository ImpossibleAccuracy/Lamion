package com.application.lamion.feature.projects.users.data

import com.application.lamion.data.database.table.project.*
import com.application.lamion.domain.model.ComparisonDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.feature.projects.users.domain.model.DeviceDomain
import com.application.lamion.feature.projects.users.domain.service.DeviceService
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl : DeviceService {
    companion object {
        const val TOP_DEVICES_COUNT = 10
        const val DEVICES_PAGE_SIZE = 50
    }

    override suspend fun getTopDevices(project: ProjectDomain): List<DeviceDomain.Partial> {
        val currentMonthActivity = EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id.count())
            .where(FunctionTable.project eq project.id)
            .let {
                wrapAsExpression<Long>(it)
            }
            .alias("currentMonthActivity")

        val prevMonthActivity = EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id.count())
            .where(FunctionTable.project eq project.id)
            .let {
                wrapAsExpression<Long>(it)
            }
            .alias("prevMonthActivity")

        return DeviceTable
            .innerJoin(EventTable)
            .innerJoin(FunctionTable)
            .innerJoin(DevicePlatformTable)
            .select(
                currentMonthActivity,
                prevMonthActivity,
                *DeviceTable.columns.toTypedArray(),
            )
            .where(
                FunctionTable.project eq project.id
            )
            .orderBy(EventTable.id.count(), SortOrder.ASC)
            .limit(TOP_DEVICES_COUNT)
            .toList()
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

    override suspend fun getDevices(
        project: ProjectDomain,
        period: TimePeriod,
        page: Long
    ): List<DeviceDomain.Detailed> {
        // TODO
        val periodStartDate = now().toLocalDateTime(TimeZone.UTC)

        val currentMonthActivity = EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id.count())
            .where(FunctionTable.project eq project.id)
            .let {
                wrapAsExpression<Long>(it)
            }
            .alias("currentMonthActivity")

        val prevMonthActivity = EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id.count())
            .where(FunctionTable.project eq project.id)
            .let {
                wrapAsExpression<Long>(it)
            }
            .alias("prevMonthActivity")

        val currentMonthErrors = ErrorTable
            .innerJoin(UserTable)
            .select(ErrorTable.id.count())
            .where(UserTable.project eq project.id)
            .let {
                wrapAsExpression<Long>(it)
            }
            .alias("currentMonthErrors")

        val prevMonthErrors = ErrorTable
            .innerJoin(UserTable)
            .select(ErrorTable.id.count())
            .where(UserTable.project eq project.id)
            .let {
                wrapAsExpression<Long>(it)
            }
            .alias("prevMonthErrors")

        return DeviceTable
            .innerJoin(EventTable)
            .innerJoin(FunctionTable)
            .innerJoin(ErrorTable)
            .select(
                currentMonthActivity,
                prevMonthActivity,
                currentMonthErrors,
                prevMonthErrors,
                *DeviceTable.columns.toTypedArray(),
            )
            .where {
                FunctionTable.project.eq(project.id)
                    .and(EventTable.createdAt greaterEq periodStartDate)
            }
            .limit(DEVICES_PAGE_SIZE, page * DEVICES_PAGE_SIZE)
            .toList()
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
}