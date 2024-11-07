package com.lamion.feature.projects.users.domain.model

import com.lamion.domain.model.ComparisonDomain

sealed interface DeviceDomain {
    val title: String
    val activity: ComparisonDomain<Long>
    val platform: String

    data class Partial(
        override val title: String,
        override val activity: ComparisonDomain<Long>,
        override val platform: String
    ) : DeviceDomain

    data class Detailed(
        override val title: String,
        override val platform: String,
        override val activity: ComparisonDomain<Long>,
        val errors: ComparisonDomain<Long>,
    ) : DeviceDomain
}
