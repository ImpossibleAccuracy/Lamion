package com.application.lamion.feature.projects.feature.data.service

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.feature.domain.service.EventService
import com.application.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service

@Service
class EventServiceImpl : EventService {
    override suspend fun getEventsCount(project: ProjectDomain, dateRange: DateRange): Long = dbQuery {
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

    override suspend fun getAverageEventsPerDay(project: ProjectDomain, dateRange: DateRange): Double = dbQuery {
        val countQuery = EventTable.id.count()

        EventTable
            .innerJoin(FunctionTable)
            .select(countQuery)
            .where(
                FunctionTable.project.eq(project.id)
                    .and(EventTable.createdAt.between(dateRange.start, dateRange.end))
                    .and(FunctionTable.deleted.eq(false))
            )
            .toList()
            .first()
            .let { result ->
                val totalEvents = result[countQuery]
                val totalDays = dateRange.totalDays

                totalEvents / totalDays.toDouble()
            }
    }
}