package com.lamion.feature.projects.settings.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.SecureRandom

object AccessKeyUtils {
    private const val accessKeyLength = 20
    private val random = SecureRandom.getInstanceStrong()
    private val allowedKeySymbols = ('A'..'Z') + ('a'..'z') + ('0'..'9')

    suspend fun generateAccessKey(): String = withContext(Dispatchers.Default) {
        (1..accessKeyLength)
            .map { allowedKeySymbols[random.nextInt(allowedKeySymbols.size)] }
            .joinToString("")
    }
}