package com.application.lamion.feature.projects.feature.data.service

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.table.project.UserTable
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.feature.domain.service.EventService
import com.application.lamion.utils.dbQuery
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit

@Service
class EventServiceImpl : EventService {
    override suspend fun getEventsCount(project: ProjectDomain, dateRange: DateRange): Long = dbQuery {
        EventTable
            .innerJoin(FunctionTable)
            .select(EventTable.id)
            .where(
                FunctionTable.project.eq(project.id)
                    .and(EventTable.createdAt.between(dateRange.start, dateRange.end))
            )
            .count()
    }

    override suspend fun getAverageEventsPerDay(project: ProjectDomain, dateRange: DateRange): Double {
        val countQuery = EventTable.id.count()

        return EventTable
            .innerJoin(UserTable)
            .select(countQuery)
            .where(
                UserTable.project.eq(project.id)
                    .and(EventTable.createdAt.between(dateRange.start, dateRange.end))
            )
            .toList()
            .first()
            .let { result ->
                val totalEvents = result[countQuery]
                val totalDays = dateRange.start.toJavaLocalDateTime()
                    .until(dateRange.end.toJavaLocalDateTime(), ChronoUnit.DAYS)

                totalEvents / totalDays.toDouble()
            }
    }
}