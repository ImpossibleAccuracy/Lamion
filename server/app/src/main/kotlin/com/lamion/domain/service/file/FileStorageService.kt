package com.lamion.domain.service.file

import com.lamion.domain.model.Id
import kotlinx.coroutines.flow.Flow
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.codec.multipart.FilePart
import java.io.File

interface FileStorageService {
    fun load(file: File): Flow<DataBuffer>

    suspend fun getFileById(id: Id): LocalFile?

    suspend fun findFileByHashOrCreate(file: FilePart): LocalFile
}