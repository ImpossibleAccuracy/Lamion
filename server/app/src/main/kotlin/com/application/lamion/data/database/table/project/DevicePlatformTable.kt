package com.application.lamion.data.database.table.project

import org.jetbrains.exposed.dao.id.IntIdTable

object DevicePlatformTable : IntIdTable("DevicePlatform") {
    val title = varchar("title", 255)
}