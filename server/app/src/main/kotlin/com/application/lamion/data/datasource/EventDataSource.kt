package com.application.lamion.data.datasource

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object EventDataSource {
    fun countEventsByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ) = EventTable
        .innerJoin(FunctionTable)
        .select(EventTable.id)
        .where(
            FunctionTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
                .and(FunctionTable.deleted.eq(false))
        )
        .count()
}
