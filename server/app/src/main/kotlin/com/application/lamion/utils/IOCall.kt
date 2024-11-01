package com.application.lamion.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> ioCall(crossinline func: suspend () -> T): T =
    withContext(Dispatchers.IO) { func() }
