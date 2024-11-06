package com.application.lamion.feature.projects.dashboard.data.datasource

import com.application.lamion.data.database.table.project.ErrorTable
import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.UserTable
import com.application.lamion.data.datasource.UserDataSource
import com.application.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.DoubleColumnType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.castTo
import org.jetbrains.exposed.sql.count

object DashboardDataSource {
    fun getUsersCount(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = UserTable
        .innerJoin(EventTable)
        .select(UserTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(UserDataSource.isUserCountingAsTotalWhere(start, end))
        )
        .having {
            UserDataSource.isUserCountingAsTotalHaving()
        }
        .groupBy(UserTable.id)
        .count()

    fun getActiveUsersCount(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = UserTable
        .innerJoin(EventTable)
        .select(UserTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(UserDataSource.isUserCountingAsActiveWhere(start, end))
        )
        .groupBy(UserTable.id)
        .having {
            UserDataSource.isUserCountingAsActiveHaving(
                sourceColumn = EventTable.id.count().castTo(DoubleColumnType()),
                projectId = projectId,
                start = start,
                end = end,
            )
        }
        .count()

    fun getErrorsCountByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = ErrorTable
        .innerJoin(UserTable)
        .select(ErrorTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(ErrorTable.createdAt.between(start, end))
        )
        .count()
}