package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id

sealed interface FunctionDto {
    val id: Id
    val title: String

    data class Partial(
        @JsonProperty("ud")
        override val id: Id,

        @JsonProperty("title")
        override val title: String,
    ) : FunctionDto

    data class Detailed(
        @JsonProperty("id")
        override val id: Id,

        @JsonProperty("title")
        override val title: String,

        @JsonProperty("total_events")
        val totalEvents: Long,

        @JsonProperty("features")
        val features: List<FeatureDto.Partial>,

        @JsonProperty("tags")
        val tags: List<Tag>,
    ) : FunctionDto {
        data class Tag(
            @JsonProperty("id")
            val id: Id,

            @JsonProperty("title")
            val title: String,
        )
    }
}
