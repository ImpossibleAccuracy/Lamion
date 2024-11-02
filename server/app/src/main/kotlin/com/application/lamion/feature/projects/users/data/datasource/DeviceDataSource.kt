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
        currentMonthActivitySubquery: ExpressionAlias<Long?>,
        prevMonthActivitySubquery: ExpressionAlias<Long?>,
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
        )
        .where(FunctionTable.project eq project.id)
        .orderBy(EventTable.id.count(), SortOrder.ASC)
        .limit(count)
        .toList()


    fun getDevicesWithActivityAndErrors(
        projectId: Id,
        currentMonthActivity: ExpressionAlias<Long?>,
        prevMonthActivity: ExpressionAlias<Long?>,
        currentMonthErrors: ExpressionAlias<Long?>,
        prevMonthErrors: ExpressionAlias<Long?>,
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
        .where(FunctionTable.project.eq(projectId))
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
                .and(EventTable.createdAt.between(start, end))
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
                .and(EventTable.createdAt.between(start, end))
        )
        .let {
            wrapAsExpression<Long>(it)
        }
        .alias(alias)
}