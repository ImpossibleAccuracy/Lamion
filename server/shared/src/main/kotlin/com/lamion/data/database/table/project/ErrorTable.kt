package com.lamion.data.database.table.project

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ErrorTable : LongIdTable("error") {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val user = reference("user_id", UserTable)
    val device = reference("device_id", DeviceTable)
    val function = optReference("function_id", com.lamion.data.database.table.project.FunctionTable)
    val message = text("message")
}