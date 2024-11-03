package com.application.lamion.feature.shared.security

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.server.security.UserHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

@JvmInline
@Deprecated("")
value class SecuredScope(
    val account: AccountDomain.Total,
)

/**
 * See [BaseController]
 */
@Deprecated("Migration to BaseController class")
suspend inline fun <T> secured(crossinline callback: suspend CoroutineScope.(SecuredScope) -> T): T = coroutineScope {
    callback(SecuredScope(UserHolder.requireAccount()))
}
