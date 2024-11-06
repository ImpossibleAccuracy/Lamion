package com.application.lamion.feature.shared.controller

import com.application.lamion.domain.exception.UnauthorizedException
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.service.LoggerUtils
import com.application.lamion.domain.service.logTimeAsync
import com.application.lamion.server.security.UserHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.springframework.beans.factory.annotation.Autowired
import kotlin.coroutines.coroutineContext

data class EndpointScope(
    @Suppress("PropertyName")
    val _account: AccountDomain.Total?,
    val delegate: CoroutineScope,
) : CoroutineScope by delegate {
    val account: AccountDomain.Total
        get() = _account ?: throw UnauthorizedException("Unauthorized")
}

abstract class BaseController {
    @Autowired
    lateinit var loggerUtils: LoggerUtils

    suspend fun <T> logTime(message: String, func: suspend () -> T): T {
        if (loggerUtils.isLoggingEnabled) {
            return loggerUtils.logTime(message, func)
        }

        return func()
    }

    fun <T> CoroutineScope.logTimeAsync(message: String, func: suspend () -> T): Deferred<T> {
        if (loggerUtils.isLoggingEnabled) {
            return logTimeAsync(loggerUtils, message, func)
        }

        return async { func() }
    }

    suspend inline fun <T> endpoint(name: String, crossinline callback: suspend EndpointScope.() -> T): T =
        logTime("${name.replaceFirstChar { it.uppercaseChar() }} overall: %s") {
            callback(
                EndpointScope(
                    _account = UserHolder.getAccount(),
                    delegate = CoroutineScope(coroutineContext),
                )
            )
        }
}