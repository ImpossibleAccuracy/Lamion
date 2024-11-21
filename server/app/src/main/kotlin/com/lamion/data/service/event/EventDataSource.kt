package com.lamion.data.service.event

import com.lamion.data.database.table.project.DevicePlatformTable
import com.lamion.data.database.table.project.DeviceTable
import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.domain.model.Id
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.castTo
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.countDistinct
import org.jetbrains.exposed.sql.kotlin.datetime.KotlinLocalDateColumnType

object EventDataSource {
    fun countEventsByCreatedBetween(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = EventTable
        .innerJoin(FunctionTable)
        .select(EventTable.id)
        .where(
            FunctionTable.project.eq(projectId)
                .and(EventTable.createdAt.between(start, end))
                .and(FunctionTable.deleted.eq(false))
        )
        .count()

    fun countEventsByFeatureIdByCreatedBetween(
        featureId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Long = EventTable
        .innerJoin(FunctionTable)
        .innerJoin(FeatureFunctionRef)
        .select(EventTable.id)
        .where(
            FeatureFunctionRef.feature.eq(featureId)
                .and(EventTable.createdAt.between(start, end))
                .and(FunctionTable.deleted.eq(false))
        )
        .count()

    fun getEventsGroupByDate(
        projectId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<LocalDate, Long> {
        val dateQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType())
        val countQuery = EventTable.id.count()

        return EventTable
            .innerJoin(FunctionTable)
            .select(dateQuery, countQuery)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
                    .and(FunctionTable.deleted.eq(false))
            )
            .groupBy(dateQuery)
            .orderBy(dateQuery)
            .toList()
            .associate {
                it[dateQuery] to it[countQuery]
            }
    }

    fun getEventsByFeatureGroupByDate(
        featureId: Id,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<LocalDate, Long> {
        val dateQuery = EventTable.createdAt.castTo(KotlinLocalDateColumnType())
        val countQuery = EventTable.id.count()

        return EventTable
            .innerJoin(FunctionTable)
            .innerJoin(FeatureFunctionRef)
            .select(dateQuery, countQuery)
            .where(
                FeatureFunctionRef.feature.eq(featureId)
                    .and(EventTable.createdAt.between(start, end))
                    .and(FunctionTable.deleted.eq(false))
            )
            .groupBy(dateQuery)
            .orderBy(dateQuery)
            .toList()
            .associate {
                it[dateQuery] to it[countQuery]
            }
    }

    fun getEventsGroupByPlatform(
        projectId: Id,
        totalEvents: Long,
        start: LocalDateTime,
        end: LocalDateTime,
    ): Map<String, Double> {
        val countQuery = EventTable.id.countDistinct()

        return DevicePlatformTable
            .innerJoin(DeviceTable)
            .innerJoin(EventTable)
            .innerJoin(FunctionTable)
            .select(
                DevicePlatformTable.title,
                countQuery
            )
            .where(
                FunctionTable.project.eq(projectId)
                    .and(EventTable.createdAt.between(start, end))
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
