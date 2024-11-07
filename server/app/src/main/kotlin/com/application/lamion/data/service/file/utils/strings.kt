package com.application.lamion.data.service.file.utils

fun Int.padStart(length: Int, padChar: Char) =
    toString().padStart(length, padChar)

suspend fun StringBuilder.replace(
    original: String,
    updates: suspend () -> String,
) {
    var i = 0

    while (i + original.length <= length) {
        for (j in i..length) {
            var equal = true

            for (k in original.indices) {
                if (original[k] != get(i + k)) {
                    equal = false
                    break
                }
            }

            if (equal) {
                replace(i, i + original.length, updates())
            }
        }

        i += 1
    }
}