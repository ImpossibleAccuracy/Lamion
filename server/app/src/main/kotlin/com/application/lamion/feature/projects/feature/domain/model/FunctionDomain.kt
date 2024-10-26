package com.application.lamion.feature.projects.feature.domain.model

import com.application.lamion.domain.model.Id

sealed interface FunctionDomain {
    val id: Id
    val title: String

    data class Partial(
        override val id: Id,
        override val title: String,
    ) : FunctionDomain

    data class Detailed(
        override val id: Id,
        override val title: String,
        val totalEvents: Long,
        val features: List<FeatureDomain>,
        val tags: List<Tag>,
    ) : FunctionDomain {
        data class Tag(
            val id: Id,
            val title: String,
        )
    }
}
