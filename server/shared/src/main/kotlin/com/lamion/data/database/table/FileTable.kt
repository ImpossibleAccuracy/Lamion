package com.lamion.data.database.table

import com.lamion.data.database.base.BaseTable

object FileTable : BaseTable("file") {
    val title = varchar("title", 255)
    val hash = varchar("hash", 255).uniqueIndex()
    val path = varchar("path", 2024)
    val type = reference("type_id", com.lamion.data.database.table.FileTypeTable)
}