package com.application.lamion.data.datasource

import com.application.lamion.data.database.table.ProjectTable
import com.application.lamion.data.database.table.project.ErrorTable
import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object EventDataSource {
    fun getEventsCountByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ) = EventTable
        .innerJoin(FunctionTable)
        .innerJoin(ProjectTable)
        .select(ErrorTable.id)
        .where(
            ProjectTable.id.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
        )
        .count()
}
