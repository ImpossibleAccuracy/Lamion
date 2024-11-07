package com.lamion.data.service.file.datasource

import com.lamion.data.database.table.FileTypeTable
import com.lamion.data.database.utils.new
import com.lamion.domain.model.Id
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

object FileDataSource {
    fun saveFile(
        hash: String,
        title: String,
        path: String,
        type: Id,
    ) = com.lamion.data.database.table.FileTable
        .new {
            it[com.lamion.data.database.table.FileTable.hash] = hash
            it[com.lamion.data.database.table.FileTable.title] = title
            it[com.lamion.data.database.table.FileTable.path] = path
            it[com.lamion.data.database.table.FileTable.type] = type
        }!!

    fun getFileById(id: Id) =
        com.lamion.data.database.table.FileTable
            .innerJoin(FileTypeTable)
            .selectAll()
            .where(
                com.lamion.data.database.table.FileTable.id.eq(id)
            )
            .firstOrNull()

    fun getFileByHash(hash: String) = com.lamion.data.database.table.FileTable
        .innerJoin(FileTypeTable)
        .select(
            FileTypeTable.mimeType,
            *com.lamion.data.database.table.FileTable.columns.toTypedArray()
        )
        .where(com.lamion.data.database.table.FileTable.hash.eq(hash))
        .firstOrNull()

    fun saveFileType(
        title: String,
        mimeType: String,
    ) = FileTypeTable
        .new {
            it[FileTypeTable.title] = title
            it[FileTypeTable.mimeType] = mimeType
        }!![FileTypeTable.id].value

    fun getFileTypeByMime(mime: String) = FileTypeTable
        .select(FileTypeTable.id)
        .where(FileTypeTable.mimeType.eq(mime))
        .firstOrNull()
        ?.get(FileTypeTable.id)?.value
}