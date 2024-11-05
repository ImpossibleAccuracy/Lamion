package com.application.lamion.feature.projects.activity.data.service

import com.application.lamion.data.database.table.project.EventTable
import com.application.lamion.data.database.table.project.FeatureTable
import com.application.lamion.data.datasource.EventDataSource
import com.application.lamion.domain.model.*
import com.application.lamion.feature.projects.activity.data.datasource.ActivityDataSource
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import com.application.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.application.lamion.utils.asyncDbQuery
import com.application.lamion.utils.dbQuery
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
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

    // TODO: regroup methods over services
    override suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int,
    ): List<FeatureWithEvents> = dbQuery {
        val totalEventsCount = EventDataSource.getEventsCountByCreatedBetween(
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