package com.application.lamion.domain.service.logger

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

interface LoggerUtils {
    val isLoggingEnabled: Boolean

    suspend fun <T> logTime(message: String, func: suspend () -> T): T
}

fun <T> CoroutineScope.logTimeAsync(
    logger: LoggerUtils,
    message: String,
    func: suspend () -> T
): Deferred<T> = async {
    logger.logTime(message, func)
}
