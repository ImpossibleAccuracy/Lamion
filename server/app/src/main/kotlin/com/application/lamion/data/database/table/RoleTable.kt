package com.application.lamion.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object RoleTable : IntIdTable("Role") {
    val title = varchar("title", 255)
}