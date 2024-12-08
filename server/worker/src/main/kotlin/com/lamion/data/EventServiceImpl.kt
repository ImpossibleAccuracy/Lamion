package com.lamion.data

import com.lamion.data.database.table.project.ErrorTable
import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FeatureTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.data.database.table.refs.FeatureFunctionRef
import com.lamion.domain.model.Id
import com.lamion.domain.model.IncomingError
import com.lamion.domain.model.IncomingEvent
import com.lamion.domain.service.EventService
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.springframework.stereotype.Service

@Service
class EventServiceImpl : EventService {
    override suspend fun logEvents(
        projectId: Id,
        userId: Id,
        deviceId: Id,
        events: List<IncomingEvent>
    ): Unit = dbQuery {
        createAllFunctions(
            distinctFunctions = events.map { it.function }.distinct(),
            projectId = projectId
        )

        linkAllFeatures(
            events = events,
            projectId = projectId
        )

        saveEvents(
            events = events,
            userId = userId,
            deviceId = deviceId,
            projectId = projectId
        )
    }

    override suspend fun logErrors(
        projectId: Id,
        userId: Id,
        deviceId: Id,
        errors: List<IncomingError>
    ): Unit = dbQuery {
        createAllFunctions(
            distinctFunctions = errors.mapNotNull { it.function }.distinct(),
            projectId = projectId
        )

        saveErrors(
            errors = errors,
            projectId = projectId,
            deviceId = deviceId,
            userId = userId,
        )
    }

    private fun createAllFunctions(
        distinctFunctions: List<String>,
        projectId: Id,
    ) {
        val count = FunctionTable
            .select(FunctionTable.id)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(FunctionTable.title.inList(distinctFunctions))
            )
            .count()

        if (count != distinctFunctions.size.toLong()) {
            FunctionTable.batchInsert(data = distinctFunctions, ignore = true) {
                this[FunctionTable.title] = it
                this[FunctionTable.project] = projectId
            }
        }
    }

    private fun linkAllFeatures(
        events: List<IncomingEvent>,
        projectId: Id,
    ) {
        createAllMissingFeatures(
            projectId = projectId,
            features = events
                .mapNotNull { it.feature }
                .distinct()
        )

        val uniqueRows = events
            .filter { it.feature != null }
            .map {
                it.function to it.feature!!
            }
            .distinct()

        FeatureFunctionRef.batchInsert(data = uniqueRows, ignore = true) {
            this[FeatureFunctionRef.function] = FunctionTable
                .select(FunctionTable.id)
                .where(
                    FunctionTable.project.eq(projectId)
                        .and(FunctionTable.title.eq(it.first))
                )

            this[FeatureFunctionRef.feature] = FeatureTable
                .select(FeatureTable.id)
                .where(
                    FeatureTable.project.eq(projectId)
                        .and(FeatureTable.title.eq(it.second))
                )
        }
    }

    private fun createAllMissingFeatures(
        features: List<String>,
        projectId: Id,
    ) = FeatureTable
        .select(FeatureTable.title)
        .where(
            FeatureTable.title.inList(features)
                .and(FeatureTable.deleted.eq(false))
        )
        .toList()
        .let { list ->
            if (list.size == features.size) return@let

            val saved = list.map { it[FeatureTable.title] }.toSet()
            val diff = features.minus(saved)

            FeatureTable.batchInsert(data = diff) {
                this[FeatureTable.title] = it
                this[FeatureTable.project] = projectId
            }
        }

    private fun saveEvents(
        events: List<IncomingEvent>,
        userId: Id,
        deviceId: Id,
        projectId: Id
    ) = EventTable.batchInsert(data = events) {
        this[EventTable.createdAt] = it.createdAt
        this[EventTable.user] = userId
        this[EventTable.device] = deviceId
        this[EventTable.function] = FunctionTable
            .select(FunctionTable.id)
            .where(
                FunctionTable.project.eq(projectId)
                    .and(FunctionTable.title.eq(it.function))
            )
    }

    private fun saveErrors(
        errors: List<IncomingError>,
        projectId: Id,
        deviceId: Id,
        userId: Id,
    ) = ErrorTable.batchInsert(data = errors) {
        this[ErrorTable.createdAt] = it.createdAt
        this[ErrorTable.user] = userId
        this[ErrorTable.device] = deviceId
        this[ErrorTable.message] = it.text

        if (it.function != null) {
            this[ErrorTable.function] = FunctionTable
                .select(FunctionTable.id)
                .where(
                    FunctionTable.project.eq(projectId)
                        .and(FunctionTable.title.eq(it.function))
                )
        }
    }
}