package com.lamion.feature.projects.function.domain

import com.lamion.domain.model.Id
import com.lamion.feature.projects.feature.domain.model.FeatureDomain

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
        val events: Long,
        val features: List<FeatureDomain.Partial>,
        val tags: List<Tag>,
    ) : FunctionDomain {
        data class Tag(
            val id: Id,
            val title: String,
        )
    }
}
