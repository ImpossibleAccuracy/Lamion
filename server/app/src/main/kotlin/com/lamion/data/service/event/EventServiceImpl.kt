package com.lamion.data.service.event

import com.lamion.domain.model.*
import com.lamion.domain.service.event.EventService
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.datetime.LocalDate
import org.springframework.stereotype.Service

@Service
class EventServiceImpl : EventService {
    override suspend fun getEventsComparison(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> = ComparisonDomain.fromDateRange(dateRange) {
        asyncDbQuery {
            EventDataSource.countEventsByCreatedBetween(
                projectId = project.id,
                start = it.start,
                end = it.end,
            )
        }
    }

    override suspend fun getEventsComparison(
        feature: FeatureDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> = ComparisonDomain.fromDateRange(dateRange) {
        asyncDbQuery {
            EventDataSource.countEventsByFeatureIdByCreatedBetween(
                featureId = feature.id,
                start = it.start,
                end = it.end,
            )
        }
    }

    override suspend fun countTotalEvents(
        project: ProjectDomain,
        dateRange: DateRange
    ): Long = dbQuery {
        EventDataSource.countEventsByCreatedBetween(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun countAverageEventsPerDay(
        project: ProjectDomain,
        dateRange: DateRange
    ): Double {
        val totalDays = dateRange.totalDays
        val totalEvents = countTotalEvents(project, dateRange)

        return totalEvents / totalDays.toDouble()
    }

    override suspend fun countEventsGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        EventDataSource.getEventsGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun countEventsGroupByDate(
        feature: FeatureDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        EventDataSource.getEventsByFeatureGroupByDate(
            featureId = feature.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun countEventsByPlatform(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<String, Double> = dbQuery {
        val totalEvents = countTotalEvents(project, dateRange)

        EventDataSource.getEventsGroupByPlatform(
            projectId = project.id,
            totalEvents = totalEvents,
            start = dateRange.start,
            end = dateRange.end,
        )
    }
}