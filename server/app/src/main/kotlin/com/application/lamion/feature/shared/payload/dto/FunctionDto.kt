package com.application.lamion.feature.shared.payload.dto

import com.application.lamion.domain.model.Id

sealed interface FunctionDto {
    val id: Id
    val title: String

    data class Partial(
        override val id: Id,
        override val title: String,
    ) : FunctionDto

    data class Detailed(
        override val id: Id,
        override val title: String,
        val totalEvents: Long,
        val features: List<FeatureDto>,
        val tags: List<Tag>,
    ) : FunctionDto {
        data class Tag(
            val id: Id,
            val title: String,
        )
    }
}
