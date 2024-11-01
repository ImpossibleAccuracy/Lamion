package com.application.lamion.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ProjectAccessKeyTable : LongIdTable("ProjectAccessKey") {
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val title = varchar("title", 255)
    val value = varchar("value", 255)
    val project = reference("project_id", ProjectTable)
}