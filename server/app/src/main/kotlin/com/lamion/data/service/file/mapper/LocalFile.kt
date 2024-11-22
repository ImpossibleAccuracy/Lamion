package com.lamion.data.service.file.mapper

import org.jetbrains.exposed.sql.ResultRow
import java.io.File

fun ResultRow.toLocalFile(mimeType: String) = com.lamion.domain.service.file.LocalFile(
    id = this[com.lamion.data.database.table.FileTable.id].value,
    title = this[com.lamion.data.database.table.FileTable.title],
    file = File(this[com.lamion.data.database.table.FileTable.path]),
    mimeType = mimeType,
)