package com.lamion.domain.model

import kotlinx.coroutines.Deferred

data class ComparisonDomain<T>(
    val actual: T,
    val past: T,
) {
    companion object {
        suspend inline fun <T> fromDateRange(
            extendedDateRange: ExtendedDateRange,
            block: (dateRange: DateRange) -> Deferred<T>
        ): ComparisonDomain<T> {
            val actual = block(extendedDateRange.second)
            val past = block(extendedDateRange.first)

            return ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }
    }
}