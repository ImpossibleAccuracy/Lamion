package com.application.lamion.feature.shared.security

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.server.security.UserHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

@JvmInline
value class SecuredScope(
    val account: AccountDomain.Total,
)

suspend inline fun <T> secured(crossinline callback: suspend CoroutineScope.(SecuredScope) -> T): T = coroutineScope {
    callback(SecuredScope(UserHolder.requireAccount()))
}
