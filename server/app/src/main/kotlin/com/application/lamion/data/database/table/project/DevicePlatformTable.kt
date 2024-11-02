package com.application.lamion.data.database.table.project

import org.jetbrains.exposed.dao.id.IntIdTable

object DevicePlatformTable : IntIdTable("device_platform") {
    val title = varchar("title", 255)
}