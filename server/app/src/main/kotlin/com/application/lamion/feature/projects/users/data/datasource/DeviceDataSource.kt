package com.application.lamion.feature.projects.users.data.datasource

import com.application.lamion.data.database.table.project.*
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object DeviceDataSource {
    fun getDevicesWithActivity(
        currentMonthActivitySubquery: Expression<Long?>,
        prevMonthActivitySubquery: Expression<Long?>,
        project: ProjectDomain,
        count: Int,
    ) = DeviceTable
        .innerJoin(EventTable)
        .innerJoin(FunctionTable)
        .innerJoin(DevicePlatformTable)
        .select(
            currentMonthActivitySubquery,
            prevMonthActivitySubquery,
            *DeviceTable.columns.toTypedArray(),
            DevicePlatformTable.title,
        )
        .where(
            FunctionTable.project.eq(project.id)
                .and(FunctionTable.deleted.eq(false))
        )
        .orderBy(currentMonthActivitySubquery, SortOrder.DESC)
        .limit(count)
        .toList()


    fun getDevicesWithActivityAndErrors(
        projectId: Id,
        currentMonthActivity: Expression<Long?>,
        prevMonthActivity: Expression<Long?>,
        currentMonthErrors: Expression<Long?>,
        prevMonthErrors: Expression<Long?>,
        limit: Int,
        offset: Long,
    ) = DeviceTable
        .innerJoin(EventTable)
        .innerJoin(FunctionTable)
        .innerJoin(ErrorTable)
        .innerJoin(DevicePlatformTable)
        .select(
            currentMonthActivity,
            prevMonthActivity,
            currentMonthErrors,
            prevMonthErrors,
            *DeviceTable.columns.toTypedArray(),
            DevicePlatformTable.title,
        )
        .where(
            FunctionTable.project.eq(projectId)
                .and(FunctionTable.deleted.eq(false))
        )
        .orderBy(currentMonthActivity, SortOrder.DESC)
        .groupBy(
            DeviceTable.id,
            DevicePlatformTable.title,
        )
        .limit(limit, offset)
        .toList()


    fun createActivitySubquery(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
        alias: String,
    ) = EventTable
        .innerJoin(FunctionTable)
        .select(EventTable.id.count())
        .where(
            FunctionTable.project.eq(projectId)
                .and(FunctionTable.deleted.eq(false))
                .and(EventTable.createdAt.between(start, end))
                .and(EventTable.device.eq(DeviceTable.id))
        )
        .let {
            wrapAsExpression<Long>(it)
        }
        .alias(alias)

    fun createErrorSubquery(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
        alias: String
    ) = ErrorTable
        .innerJoin(UserTable)
        .select(ErrorTable.id.count())
        .where(
            UserTable.project.eq(projectId)
                .and(ErrorTable.createdAt.between(start, end))
                .and(ErrorTable.device.eq(DeviceTable.id))
        )
        .let {
            wrapAsExpression<Long>(it)
        }
        .alias(alias)
}