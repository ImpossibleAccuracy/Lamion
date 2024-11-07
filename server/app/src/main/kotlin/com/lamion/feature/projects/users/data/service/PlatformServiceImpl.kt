package com.lamion.feature.projects.users.data.service

import com.lamion.data.database.table.project.DevicePlatformTable
import com.lamion.data.database.table.project.DeviceTable
import com.lamion.data.database.table.project.EventTable
import com.lamion.domain.model.ChartDomain
import com.lamion.domain.model.DateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.event.EventService
import com.lamion.feature.projects.users.domain.service.PlatformService
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.countDistinct
import org.springframework.stereotype.Service

@Service
class PlatformServiceImpl(
    private val eventService: EventService,
) : PlatformService {
    override suspend fun countEventsGroupByPlatforms(project: ProjectDomain, dateRange: DateRange): ChartDomain<String, Double> =
        dbQuery {
            val countQuery = EventTable.id.countDistinct()

            val totalEvents = eventService.countTotalEvents(project, dateRange)

            DevicePlatformTable
                .innerJoin(DeviceTable)
                .innerJoin(EventTable)
                .innerJoin(com.lamion.data.database.table.project.FunctionTable)
                .select(
                    DevicePlatformTable.title,
                    countQuery
                )
                .where(
                    com.lamion.data.database.table.project.FunctionTable.project.eq(project.id)
                        .and(EventTable.createdAt.between(dateRange.start, dateRange.end))
                )
                .groupBy(DevicePlatformTable.title)
                .toList()
                .associate {
                    val platformEvents = it[countQuery]
                    val percentOfTotalEvents = platformEvents * 100.0 / totalEvents

                    it[DevicePlatformTable.title] to percentOfTotalEvents
                }
        }
}