package com.lamion.feature.projects.feature.data.service

import com.lamion.data.database.table.project.EventTable
import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.event.EventService
import com.lamion.feature.projects.feature.data.datasource.FeatureDataSource
import com.lamion.feature.projects.feature.data.mapper.toFeatureDomainPartial
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.projects.feature.domain.service.FeatureDashboardService
import com.lamion.utils.dbQuery
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.count
import org.springframework.stereotype.Service

@Service
class FeatureDashboardServiceImpl(
    private val eventService: EventService,
) : FeatureDashboardService {
    override suspend fun countAverageEventsPerDay(project: ProjectDomain, dateRange: DateRange): Double {
        val totalDays = dateRange.totalDays
        val totalEvents = eventService.countTotalEvents(project, dateRange)

        return totalEvents / totalDays.toDouble()
    }

    override suspend fun countEventsGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        FeatureDataSource.getEventsGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun getTopFeatures(
        project: ProjectDomain,
        dateRange: DateRange,
        count: Int
    ): ChartDomain<FeatureDomain.Partial, Long> = dbQuery {
        val eventsCountQuery = EventTable.id.count().alias("eventsCount")

        FeatureDataSource
            .findFeaturesOrderByEventsCount(
                projectId = project.id,
                eventsCountQuery = eventsCountQuery,
                start = dateRange.start,
                end = dateRange.end,
                count = count,
            )
            .toList()
            .associate {
                it.toFeatureDomainPartial() to it[eventsCountQuery]
            }
    }
}