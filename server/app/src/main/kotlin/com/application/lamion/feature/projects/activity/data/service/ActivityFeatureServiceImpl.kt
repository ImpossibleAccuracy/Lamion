package com.application.lamion.feature.projects.activity.data.service

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.datasource.EventDataSource
import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.activity.data.datasource.ActivityDataSource
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import com.application.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.application.lamion.utils.*
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service

@Service
class ActivityFeatureServiceImpl : ActivityFeatureService {
    override suspend fun getProjectActivity(
        project: ProjectDomain,
        start: LocalDateTime,
        end: LocalDateTime?,
    ): List<CalendarItemDomain> {
        val endDate = end ?: LocalDateTime.now()
        val startOfMonth = start.date.atStartOfMonth()

        val users = asyncDbQuery {
            ActivityDataSource
                .getUserActivityInfo(
                    projectId = project.id,
                    start = start,
                    end = endDate
                )
                .map { (count, day) ->
                    Triple(
                        startOfMonth.plus(DatePeriod(days = day)),
                        count,
                        CalendarItemDomain.Type.USERS
                    )
                }
        }

        val events = asyncDbQuery {
            ActivityDataSource
                .getEventActivityInfo(
                    projectId = project.id,
                    start = start,
                    end = endDate
                )
                .map { (count, day) ->
                    Triple(
                        startOfMonth.plus(DatePeriod(days = day)),
                        count,
                        CalendarItemDomain.Type.EVENTS
                    )
                }
        }

        val errors = asyncDbQuery {
            ActivityDataSource
                .getErrorActivityInfo(
                    projectId = project.id,
                    start = start,
                    end = endDate
                )
                .map { (count, day) ->
                    Triple(
                        startOfMonth.plus(DatePeriod(days = day)),
                        count,
                        CalendarItemDomain.Type.ERRORS
                    )
                }
        }

        return users.await()
            .plus(events.await())
            .plus(errors.await())
            .groupBy { it.first }
            .map { (date, info) ->
                CalendarItemDomain(
                    date = date,
                    activity = info.associate {
                        it.third to it.second
                    }
                )
            }
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

        // TODO: make count users instead of events
        ActivityDataSource
            .getUserActivityTime(
                projectId = project.id,
                startDate = startDate,
                endDate = endDate
            )
            .associate { (hour, count) ->
                Pair(
                    LocalTime(hour = hour, minute = 0, second = 0),
                    count
                )
            }
    }
}