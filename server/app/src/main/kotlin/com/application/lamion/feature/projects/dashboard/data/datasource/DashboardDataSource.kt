package com.application.lamion.feature.projects.dashboard.data.datasource

import com.application.lamion.data.database.table.ProjectTable
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
    fun getTotalUsersCountByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime
    ): Long = UserTable
        .innerJoin(ProjectTable)
        .select(UserTable.id)
        .where(
            ProjectTable.id.eq(projectId)
                .and(UserTable.createdAt.between(start, end))
        )
        .count()

    fun getActiveUsersCountByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
        minEventsToActive: Long,
    ): Long {
        val eventsCount = EventTable.id.count()

        return UserTable
            .innerJoin(EventTable)
            .select(UserTable.id)
            .where(
                UserTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
            )
            .having {
                eventsCount greaterEq minEventsToActive
            }
            .groupBy(UserTable.id)
            .count()
    }

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