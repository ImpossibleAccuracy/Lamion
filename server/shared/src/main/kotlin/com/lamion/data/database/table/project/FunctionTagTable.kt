package com.lamion.data.database.table.project

import org.jetbrains.exposed.dao.id.LongIdTable

object FunctionTagTable : LongIdTable("function_tag") {
    val title = varchar("title", 255)
}