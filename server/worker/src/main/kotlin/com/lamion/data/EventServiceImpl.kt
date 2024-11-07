package com.lamion.data

import com.lamion.data.database.table.project.EventTable
import com.lamion.data.database.table.project.FunctionTable
import com.lamion.domain.EventService
import com.lamion.domain.model.Id
import com.lamion.utils.dbQuery
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.springframework.stereotype.Service

@Service
class EventServiceImpl : EventService {
    override suspend fun logEvents(
        projectId: Id,
        userId: Id,
        deviceId: Id,
        events: List<Pair<String, LocalDateTime>>
    ) {
        dbQuery {
            val distinctFunctions = events.map { it.first }.distinct()

            // Make sure all function created
            FunctionTable.batchInsert(data = distinctFunctions, ignore = true) {
                this[FunctionTable.title] = it
                this[FunctionTable.project] = projectId
            }

            // Insert events
            EventTable.batchInsert(data = events) {
                this[EventTable.createdAt] = it.second
                this[EventTable.user] = userId
                this[EventTable.device] = deviceId
                this[EventTable.function] = FunctionTable
                    .select(FunctionTable.id)
                    .where(
                        FunctionTable.project.eq(projectId)
                            .and(FunctionTable.title.eq(it.first))
                    )
            }
        }
    }
}