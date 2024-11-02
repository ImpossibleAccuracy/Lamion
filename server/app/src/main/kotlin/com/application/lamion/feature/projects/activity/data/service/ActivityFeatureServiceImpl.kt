package com.application.lamion.feature.projects.activity.data.service

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.database.table.project.FunctionTable
import com.application.lamion.data.database.utils.datePart
import com.application.lamion.data.datasource.EventDataSource
import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.activity.data.datasource.ActivityDataSource
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import com.application.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.application.lamion.utils.dbQuery
import com.application.lamion.utils.now
import com.application.lamion.utils.toDateTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service

@Service
class ActivityFeatureServiceImpl : ActivityFeatureService {
    override suspend fun getProjectActivity(project: ProjectDomain, month: LocalDate): List<CalendarItemDomain> {
        // Make 3 queries with unique filtering, counting, etc.

        // TODO("Not yet implemented")

        return listOf()
    }

    /**
     * @return TODO replace [ActivityDetails] with [CalendarItemDomain]
     */
    override suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails {
        // Make 3 queries with unique filtering, counting, etc.
        // and search by single date
        TODO("Not yet implemented")
    }

    override suspend fun getTopFeatures(
        project: ProjectDomain,
        start: LocalDateTime,
        end: LocalDateTime?,
        count: Int,
    ): List<FeatureWithEvents> = dbQuery {
        val endDate = end ?: LocalDateTime.now()

        val totalEventsCount = EventDataSource.getEventsCountByCreatedBetween(
            projectId = project.id,
            start = start,
            end = endDate,
        )

        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        ActivityDataSource
            .getFeatureWithTotalEventsCount(
                eventsCountQuery = eventsCountQuery,
                projectId = project.id,
                start = start,
                end = endDate,
                count = count,
            )
            .map {
                val totalEventsResult = it[eventsCountQuery]

                FeatureWithEvents(
                    id = it[FeatureTable.id].value,
                    title = it[FeatureTable.title],
                    description = it[FeatureTable.description],
                    totalEvents = totalEventsResult,
                    totalEventsPercent = totalEventsResult * 100.0 / totalEventsCount
                )
            }
    }

    override suspend fun getUserActivityTime(
        project: ProjectDomain,
        start: LocalDate,
        end: LocalDate?
    ): ChartDomain<LocalTime, Long> = dbQuery {
        val startDate = start.toDateTime()
        val endDate = end?.toDateTime() ?: LocalDateTime.now()

        val partQuery = EventTable.createdAt.datePart("hour")
        val countQuery = EventTable.id.count().alias("count")

        EventTable
            .innerJoin(FunctionTable)
            .select(partQuery, countQuery)
            .where(
                FunctionTable.project.eq(project.id)
                    .and(EventTable.createdAt.between(startDate, endDate))
            )
            .groupBy(partQuery)
            .toList()
            .sortedBy { it[partQuery] }
            .associate {
                val hour = it[partQuery]
                val count = it[countQuery]

                Pair(
                    LocalTime(hour = hour, minute = 0, second = 0),
                    count
                )
            }
    }
}