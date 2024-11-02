package com.application.lamion.feature.shared.payload.dto

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty

sealed interface FunctionDto {
    val id: Id
    val title: String

    data class Partial(
        @field:JsonProperty("ud")
        override val id: Id,

        @field:JsonProperty("title")
        override val title: String,
    ) : FunctionDto

    data class Detailed(
        @field:JsonProperty("id")
        override val id: Id,

        @field:JsonProperty("title")
        override val title: String,

        @field:JsonProperty("total_events")
        val totalEvents: Long,

        @field:JsonProperty("features")
        val features: List<FeatureDto>,

        @field:JsonProperty("tags")
        val tags: List<Tag>,
    ) : FunctionDto {
        data class Tag(
            @field:JsonProperty("id")
            val id: Id,

            @field:JsonProperty("title")
            val title: String,
        )
    }
}
