package com.lamion.feature.projects.feature.domain.model

import com.lamion.domain.model.Id

sealed interface FeatureDomain {
    val id: Id
    val title: String
    val description: String?

    data class Partial(
        override val id: Id,
        override val title: String,
        override val description: String?,
    ) : FeatureDomain

    data class Detailed(
        override val id: Id,
        override val title: String,
        override val description: String?,
        val totalFunctions: Long,
        val totalEvents: Long,
        val errors: Long,
        val topFunctions: List<TopFunction>,
    ) : FeatureDomain {
        data class TopFunction(
            val id: Id,
            val title: String,
            val totalEvents: Long,
            val percent: Double,
        )
    }
}
