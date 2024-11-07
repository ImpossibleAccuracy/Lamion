package com.lamion.data.datasource

import com.lamion.data.database.table.project.ErrorTable
import com.lamion.data.database.table.project.UserTable
import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

object ErrorDataSource {
    fun countErrorsByCreatedBetween(
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