package com.application.lamion.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.codec.multipart.FilePart


suspend fun FilePart.byteContent() = withContext(Dispatchers.Default) {
    val dataBuffer = DataBufferUtils
        .join(content())
        .awaitSingle()

    ByteArray(dataBuffer.readableByteCount()).apply {
        dataBuffer.read(this)
        DataBufferUtils.release(dataBuffer)
    }
}