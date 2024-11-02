package com.application.lamion.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object RoleTable : IntIdTable("role") {
    val title = varchar("title", 255)
}