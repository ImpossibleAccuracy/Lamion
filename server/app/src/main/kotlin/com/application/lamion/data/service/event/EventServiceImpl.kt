package com.application.lamion.data.service.event

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.domain.service.event.EventService
import com.application.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Service

@Service
class EventServiceImpl : EventService {
    override suspend fun countTotalEvents(project: ProjectDomain, dateRange: DateRange): Long = dbQuery {
        EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id)
            .where(
                FunctionTable.project.eq(project.id)
                    .and(EventTable.createdAt.between(dateRange.start, dateRange.end))
                    .and(FunctionTable.deleted.eq(false))
            )
            .count()
    }
}