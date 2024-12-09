package com.lamion.data.service.common

import com.lamion.domain.model.ComparisonDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.model.TimePeriod
import com.lamion.domain.service.analytics.AnalyticsService
import com.lamion.domain.service.event.EventService
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.atStartOfDay
import com.lamion.utils.now
import kotlinx.datetime.LocalDateTime
import org.springframework.stereotype.Service

@Service
class AnalyticsServiceImpl(
    private val eventService: EventService,
) : AnalyticsService {
    override suspend fun computeGrowthRate(project: ProjectDomain, timePeriod: TimePeriod): ComparisonDomain<Double>? {
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

        if (period1 == 0.0 || period2 == 0.0 || period3 == 0.0) return null

        return ComparisonDomain(
            actual = (period3 / period2 - 1) * 100,
            past = (period2 / period1 - 1) * 100
        )
    }
}