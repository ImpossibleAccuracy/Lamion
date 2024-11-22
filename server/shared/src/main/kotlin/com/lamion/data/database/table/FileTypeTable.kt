package com.lamion.data.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object FileTypeTable : LongIdTable("file_type") {
    val title = varchar("title", 255)
    val mimeType = varchar("mime_type", 255)
}