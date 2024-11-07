package com.lamion.data.service.file

import com.lamion.data.database.table.FileTypeTable
import com.lamion.data.service.file.datasource.FileDataSource
import com.lamion.data.service.file.mapper.toLocalFile
import com.lamion.data.service.file.utils.replace
import com.lamion.domain.model.Id
import com.lamion.utils.byteContent
import com.lamion.utils.dbQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.core.io.UrlResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.io.File
import java.net.MalformedURLException
import java.net.URLConnection
import java.nio.file.Paths
import java.security.MessageDigest
import java.time.LocalDateTime
import kotlin.io.path.createDirectories


@Service
class FileStorageServiceImpl(
    private val properties: com.lamion.data.service.file.FileStorageProperties,
) : com.lamion.domain.service.file.FileStorageService {
    companion object {
        private val Logger = LoggerFactory.getLogger(com.lamion.data.service.file.FileStorageServiceImpl::class.java)

        private const val MAX_ORIGINAL_NAME_LENGTH = 15

        private const val FILE_NAME_PATTERN = "{FILE_NAME}"
        private const val FILE_HASH_PATTERN = "{FILE_HASH}"
        private const val TIMESTAMP_PATTERN = "{TIMESTAMP}"
        private const val EXTENSION_PATTERN = "{EXTENSION}"
    }

    private val rootPath = Paths.get(properties.storePath)

    init {
        @Suppress("OPT_IN_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            rootPath.createDirectories()
        }
    }

    override fun load(file: File): Flow<DataBuffer> {
        try {
            val resource = UrlResource(file.toURI())

            if (resource.exists() || resource.isReadable) {
                return DataBufferUtils
                    .read(resource, DefaultDataBufferFactory(), 4096)
                    .asFlow()
            } else {
                throw RuntimeException("File not found")
            }
        } catch (e: MalformedURLException) {
            throw RuntimeException("Error: " + e.message)
        }
    }

    override suspend fun getFileById(id: Id): com.lamion.domain.service.file.LocalFile? = dbQuery {
        FileDataSource
            .getFileById(id)
            ?.let {
                it.toLocalFile(
                    mimeType = it[FileTypeTable.mimeType]
                )
            }
    }

    override suspend fun findFileByHashOrCreate(file: FilePart): com.lamion.domain.service.file.LocalFile {
        val content = file.byteContent()
        val hash = computeFileHash(content)

        dbQuery {
            FileDataSource
                .getFileByHash(hash)
                ?.let {
                    it.toLocalFile(
                        mimeType = it[FileTypeTable.mimeType]
                    )
                }
        }?.let {
            return it
        }

        val newFile = store(file, hash)

        return dbQuery {
            val (fileType, fileMimeType) = getFileType(newFile)

            FileDataSource
                .saveFile(
                    hash = hash,
                    title = file.filename() ?: newFile.nameWithoutExtension,
                    path = newFile.absolutePath,
                    type = fileType
                )
                .toLocalFile(mimeType = fileMimeType)
        }
    }


    private suspend fun getFileType(file: File): Pair<Id, String> =
        URLConnection.guessContentTypeFromName(file.name).let { mimeType: String? ->
            val actualMimeType = mimeType ?: "image/png"

            dbQuery {
                FileDataSource
                    .getFileTypeByMime(actualMimeType)
                    ?: FileDataSource.saveFileType(
                        title = "From media type $actualMimeType",
                        mimeType = actualMimeType,
                    )
            }.let {
                it to actualMimeType
            }
        }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun computeFileHash(bytes: ByteArray): String = withContext(Dispatchers.Default) {
        val md = MessageDigest.getInstance("MD5")

        val digest = md.digest(bytes)

        digest.toHexString()
    }

    suspend fun store(file: FilePart, hash: String): File {
        val originalName = file.filename() ?: file.name()
        val fileName = computeFileName(originalName, hash)

        val destination = rootPath.resolve(fileName)

        withContext(Dispatchers.IO) {
            file.transferTo(destination).awaitSingleOrNull()
        }

        val resultFile = destination.toFile()

        com.lamion.data.service.file.FileStorageServiceImpl.Companion.Logger.info(
            "File ${cutFileName(originalName)} stored as ${resultFile.absoluteFile}"
        )

        return resultFile
    }

    private suspend fun computeFileName(originalFileName: String, hash: String): String {
        return buildString {
            append(properties.fileNamePattern)

            replace(com.lamion.data.service.file.FileStorageServiceImpl.Companion.FILE_NAME_PATTERN) {
                val string = originalFileName.substringBeforeLast(".")

                cutFileName(string)
            }

            replace(com.lamion.data.service.file.FileStorageServiceImpl.Companion.FILE_HASH_PATTERN) {
                hash
            }

            replace(com.lamion.data.service.file.FileStorageServiceImpl.Companion.TIMESTAMP_PATTERN) {
                LocalDateTime.now().toString()
            }

            replace(com.lamion.data.service.file.FileStorageServiceImpl.Companion.EXTENSION_PATTERN) {
                originalFileName.substringAfterLast(".")
            }

            replace(":") { "-" }
        }
    }

    private fun cutFileName(fileName: String) =
        if (fileName.length > com.lamion.data.service.file.FileStorageServiceImpl.Companion.MAX_ORIGINAL_NAME_LENGTH) fileName.substring(0,
            com.lamion.data.service.file.FileStorageServiceImpl.Companion.MAX_ORIGINAL_NAME_LENGTH
        )
        else fileName

    /*suspend fun move(file: File): File = withContext(Dispatchers.Default) {
        val filePath = file.toPath()

        if (filePath.contains(rootPath)) {
            return@withContext file
        }

        val resultPath = withContext(Dispatchers.IO) {
            Files.move(
                filePath,
                rootPath.resolve(file.name),
            )
        }

        return@withContext resultPath.toFile()
            .also { resultFile ->
                Logger.info(
                    "File ${cutFileName(file.name)} moved into store as ${resultFile.absoluteFile}"
                )
            }
    }

    suspend fun delete(file: File): Boolean {
        val catalogPath = rootPath.toAbsolutePath()
        val filePath = file.toPath().toAbsolutePath()

        if (!filePath.startsWith(catalogPath)) {
            return false
        }

        return file.delete().also {
            if (it) {
                Logger.info("File ${cutFileName(file.name)} deleted")
            }
        }
    }*/
}
