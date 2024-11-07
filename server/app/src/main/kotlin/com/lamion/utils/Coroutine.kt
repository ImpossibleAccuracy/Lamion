package com.lamion.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync

suspend fun <T> dbQuery(func: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { func() }

suspend fun <T> asyncDbQuery(func: suspend CoroutineScope.() -> T): Deferred<T> =
    suspendedTransactionAsync(Dispatchers.IO) {
        coroutineScope {
            func()
        }
    }
