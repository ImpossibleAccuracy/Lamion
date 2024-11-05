package com.application.lamion.feature.projects.dashboard.data.datasource

import com.application.lamion.data.database.table.project.ErrorTable
import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.UserTable
import com.application.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count

object DashboardDataSource {
    fun getUsersCountByEventsCount(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
        minEventsToActive: Long?, // TODO: replace fixed number to percent of total events by this period
    ): Long = UserTable
        .innerJoin(EventTable)
        .select(UserTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
        )
        .let {
            if (minEventsToActive == null) it
            else it.having {
                EventTable.id.count() greaterEq minEventsToActive
            }
        }
        .groupBy(UserTable.id)
        .count()

    fun getErrorsCountByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ) = ErrorTable
        .innerJoin(UserTable)
        .select(ErrorTable.id)
        .where(
            UserTable.project.eq(projectId)
                .and(ErrorTable.createdAt.between(start, end))
        )
        .count()
}