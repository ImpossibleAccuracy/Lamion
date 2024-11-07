package com.lamion.data.datasource

import com.lamion.data.database.table.project.EventTable
import com.lamion.domain.model.Id
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
        .innerJoin(com.lamion.data.database.table.project.FunctionTable)
        .select(EventTable.id)
        .where(
            com.lamion.data.database.table.project.FunctionTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
                .and(com.lamion.data.database.table.project.FunctionTable.deleted.eq(false))
        )
        .count()
}
