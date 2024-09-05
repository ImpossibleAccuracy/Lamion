package com.application.lamion.util

import at.favre.lib.crypto.bcrypt.BCrypt

object Password {
    private const val COST = 12

    fun hash(plain: String): String {
        return BCrypt.withDefaults().hashToString(12, plain.toCharArray())
    }

    fun verify(plain: String, hash: String): Boolean {
        val result = BCrypt.verifyer().verify(plain.toCharArray(), hash)
        return result.verified
    }
}
