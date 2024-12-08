package com.lamion.feature.projects.activity.data.service

import com.lamion.domain.model.CalendarItemDomain
import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.errors.ErrorsService
import com.lamion.domain.service.event.EventService
import com.lamion.domain.service.users.UsersService
import com.lamion.feature.projects.activity.data.datasource.ActivityDataSource
import com.lamion.feature.projects.activity.domain.model.ActivityDetails
import com.lamion.feature.projects.activity.domain.service.ExtendedActivityService
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.atStartOfDay
import com.lamion.utils.dbQuery
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import org.springframework.stereotype.Service

@Service
class ExtendedActivityServiceImpl(
    private val usersService: UsersService,
    private val eventService: EventService,
    private val errorsService: ErrorsService,
) : ExtendedActivityService {
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
        val dateRange = DateRange(startDate, endDate)

        val eventsCountDeferred = asyncDbQuery {
            eventService.countTotalEvents(
                project = project,
                dateRange = dateRange
            )
        }

        val errorsCountDeferred = asyncDbQuery {
            errorsService.countTotalErrors(
                project = project,
                dateRange = dateRange
            )
        }

        val activeUsersCountDeferred = asyncDbQuery {
            usersService.countActiveUsers(
                project = project,
                dateRange = dateRange
            )
        }

        return ActivityDetails(
            date = date,
            activeUsers = activeUsersCountDeferred.await(),
            totalEvents = eventsCountDeferred.await(),
            crashes = errorsCountDeferred.await(),
        )
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