package com.lamion.feature.shared.utils

import com.lamion.domain.exception.ResourceNotFoundException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> T?.require(lazyMessage: () -> String): T {
    contract {
        returns() implies (this@require != null)
    }

    if (this == null) throw ResourceNotFoundException(lazyMessage())

    return this
}
