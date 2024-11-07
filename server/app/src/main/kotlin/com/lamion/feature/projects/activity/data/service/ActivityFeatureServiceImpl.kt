package com.lamion.feature.projects.activity.data.service

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.datasource.ErrorDataSource
import com.lamion.data.datasource.EventDataSource
import com.lamion.data.datasource.UserDataSource
import com.lamion.domain.model.*
import com.lamion.feature.projects.activity.data.datasource.ActivityDataSource
import com.lamion.feature.projects.activity.domain.model.ActivityDetails
import com.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.atStartOfDay
import com.lamion.utils.dbQuery
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service

@Service
class ActivityFeatureServiceImpl : ActivityFeatureService {
    override suspend fun getProjectActivity(
        project: ProjectDomain,
        dateRange: DateRange,
    ): List<CalendarItemDomain> {
        val users = asyncDbQuery {
            ActivityDataSource
                .getUserActivityInfo(
                    projectId = project.id,
                    start = dateRange.start,
                    end = dateRange.end
                )
                .map { (count, date) ->
                    Triple(
                        count,
                        date,
                        CalendarItemDomain.Type.USERS
                    )
                }
        }

        val events = asyncDbQuery {
            ActivityDataSource
                .getEventActivityInfo(
                    projectId = project.id,
                    start = dateRange.start,
                    end = dateRange.end
                )
                .map { (count, date) ->
                    Triple(
                        count,
                        date,
                        CalendarItemDomain.Type.EVENTS
                    )
                }
        }

        val errors = asyncDbQuery {
            ActivityDataSource
                .getErrorActivityInfo(
                    projectId = project.id,
                    start = dateRange.start,
                    end = dateRange.end
                )
                .map { (count, date) ->
                    Triple(
                        count,
                        date,
                        CalendarItemDomain.Type.ERRORS
                    )
                }
        }

        return users.await()
            .asSequence()
            .plus(events.await())
            .plus(errors.await())
            .sortedBy { it.first }
            .groupBy { it.first }
            .map { (date, info) ->
                CalendarItemDomain(
                    date = date,
                    activity = info.associate {
                        it.third to it.second
                    }
                )
            }
            .toList()
    }

    override suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails {
        val startDate = date.atStartOfDay()
        val endDate = date.plus(DatePeriod(days = 1)).atStartOfDay()

        val eventsCountDeferred = asyncDbQuery {
            EventDataSource.countEventsByCreatedBetween(
                projectId = project.id,
                start = startDate,
                end = endDate,
            )
        }

        val errorsCountDeferred = asyncDbQuery {
            ErrorDataSource.countErrorsByCreatedBetween(
                projectId = project.id,
                start = startDate,
                end = endDate,
            )
        }

        val activeUsersCountDeferred = asyncDbQuery {
            UserDataSource.getActiveUsersCount(
                projectId = project.id,
                start = startDate,
                end = endDate,
            )
        }

        return ActivityDetails(
            date = date,
            activeUsers = activeUsersCountDeferred.await(),
            totalEvents = eventsCountDeferred.await(),
            crashes = errorsCountDeferred.await(),
        )
    }

    // TODO: regroup methods over services
    override suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int,
    ): List<FeatureWithEvents> = dbQuery {
        val totalEventsCount = EventDataSource.countEventsByCreatedBetween(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )

        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        ActivityDataSource
            .getFeatureWithTotalEventsCount(
                eventsCountQuery = eventsCountQuery,
                projectId = project.id,
                start = dateRange.start,
                end = dateRange.end,
                count = count,
            )
            .map {
                val totalEventsResult = it[eventsCountQuery]

                FeatureWithEvents(
                    id = it[com.lamion.data.database.table.project.FeatureTable.id].value,
                    title = it[com.lamion.data.database.table.project.FeatureTable.title],
                    description = it[com.lamion.data.database.table.project.FeatureTable.description],
                    totalEvents = totalEventsResult,
                    totalEventsPercent = totalEventsResult * 100.0 / totalEventsCount
                )
            }
    }

    override suspend fun getUserActivityTime(
        project: ProjectDomain,
        dateRange: DateRange,
    ): ChartDomain<LocalTime, Long> = dbQuery {
        ActivityDataSource
            .getUserActivityTime(
                projectId = project.id,
                startDate = dateRange.start,
                endDate = dateRange.end,
            )
            .associate { (hour, count) ->
                Pair(
                    LocalTime(hour = hour, minute = 0, second = 0),
                    count
                )
            }
    }
}