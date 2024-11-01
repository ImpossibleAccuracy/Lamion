package com.application.lamion.data.database.table.project

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ErrorTable : LongIdTable("Error") {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val user = reference("user_id", UserTable)
    val device = reference("device_id", DeviceTable)
    val message = text("message")
}