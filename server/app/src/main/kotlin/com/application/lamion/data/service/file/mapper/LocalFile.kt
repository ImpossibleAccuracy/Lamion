package com.application.lamion.data.service.file.mapper

import com.application.lamion.data.database.table.FileTable
import com.application.lamion.domain.service.file.LocalFile
import org.jetbrains.exposed.sql.ResultRow
import java.io.File

fun ResultRow.toLocalFile(mimeType: String) = LocalFile(
    id = this[FileTable.id].value,
    title = this[FileTable.title],
    file = File(this[FileTable.path]),
    mimeType = mimeType,
)