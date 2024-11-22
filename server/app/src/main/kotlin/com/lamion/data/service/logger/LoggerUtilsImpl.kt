package com.lamion.data.service.logger

import com.lamion.domain.service.logger.LoggerUtils
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import kotlin.time.measureTime

@Component
class LoggerUtilsImpl(
    private val environment: Environment,
) : LoggerUtils {
    companion object {
        const val LOGGER_PROFILE_NAME = "logging"
    }

    private val logger = LoggerFactory.getLogger(LoggerUtils::class.java)

    override val isLoggingEnabled: Boolean
        get() = environment.activeProfiles.contains(LOGGER_PROFILE_NAME)

    override suspend fun <T> logTime(message: String, func: suspend () -> T): T {
        if (isLoggingEnabled) {
            val result: T

            measureTime {
                result = func.invoke()
            }.let {
                logger.info(message.format(it))
            }

            return result
        } else {
            return func()
        }
    }
}