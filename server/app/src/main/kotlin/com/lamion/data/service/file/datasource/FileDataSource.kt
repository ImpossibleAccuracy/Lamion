package com.lamion.data.service.file.datasource

import com.lamion.data.database.table.FileTable
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
    ) = FileTable
        .new {
            it[FileTable.hash] = hash
            it[FileTable.title] = title
            it[FileTable.path] = path
            it[FileTable.type] = type
        }!!

    fun getFileById(id: Id) =
        FileTable
            .innerJoin(FileTypeTable)
            .selectAll()
            .where(
                FileTable.id.eq(id)
            )
            .firstOrNull()

    fun getFileByHash(hash: String) = FileTable
        .innerJoin(FileTypeTable)
        .select(
            FileTypeTable.mimeType,
            *FileTable.columns.toTypedArray()
        )
        .where(FileTable.hash.eq(hash))
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