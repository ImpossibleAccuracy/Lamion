package com.application.lamion.feature.projects.users.data.service

import com.application.lamion.domain.model.*
import com.application.lamion.domain.service.EventService
import com.application.lamion.feature.projects.users.data.datasource.UserFeatureDataSource
import com.application.lamion.feature.projects.users.domain.service.UsersDashboardService
import com.application.lamion.utils.asyncDbQuery
import com.application.lamion.utils.atStartOfDay
import com.application.lamion.utils.dbQuery
import com.application.lamion.utils.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.springframework.stereotype.Service

@Service
class UsersDashboardServiceImpl(
    private val eventService: EventService,
) : UsersDashboardService {
    override suspend fun countUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        UserFeatureDataSource.getUserCountGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun countActiveUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        UserFeatureDataSource.getActiveUsersGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun computeGrowthRate(
        project: ProjectDomain,
        timePeriod: TimePeriod
    ): ComparisonDomain<Double> {
        val start = timePeriod.toLocalDate(multiplier = 3).atStartOfDay()
        val step1 = timePeriod.toLocalDate(multiplier = 2).atStartOfDay()
        val step2 = timePeriod.toLocalDate(multiplier = 1).atStartOfDay()
        val end = LocalDateTime.now()

        val period1Deferred = asyncDbQuery {
            eventService.countTotalEvents(
                project = project,
                dateRange = DateRange(start, step1)
            )
        }

        val period2Deferred = asyncDbQuery {
            eventService.countTotalEvents(
                project = project,
                dateRange = DateRange(step1, step2)
            )
        }

        val period3Deferred = asyncDbQuery {
            eventService.countTotalEvents(
                project = project,
                dateRange = DateRange(step2, end)
            )
        }

        val period1 = period1Deferred.await().toDouble()
        val period2 = period2Deferred.await().toDouble()
        val period3 = period3Deferred.await().toDouble()

        return ComparisonDomain(
            actual = (period3 / period2 - 1) * 100,
            past = (period2 / period1 - 1) * 100
        )
    }
}