package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id

sealed interface FeatureDto {
    val id: Id
    val title: String
    val description: String?

    data class Partial(
        @JsonProperty("id")
        override val id: Id,

        @JsonProperty("title")
        override val title: String,

        @JsonProperty("description")
        override val description: String?,
    ) : FeatureDto

    data class WithEvents(
        @JsonProperty("id")
        override val id: Id,

        @JsonProperty("title")
        override val title: String,

        @JsonProperty("description")
        override val description: String?,

        @JsonProperty("total_events")
        val totalEvents: Long,

        @JsonProperty("total_events_percent")
        val totalEventsPercent: Double,
    ) : FeatureDto

    data class Detailed(
        @JsonProperty("id")
        override val id: Id,

        @JsonProperty("title")
        override val title: String,

        @JsonProperty("description")
        override val description: String?,

        @JsonProperty("functions_count")
        val functionsCount: Long,

        @JsonProperty("total_events")
        val totalEvents: Long,

        @JsonProperty("total_errors")
        val totalErrors: Long,

        @JsonProperty("top_function")
        val topFunction: List<TopFunction>,
    ) : FeatureDto {
        data class TopFunction(
            @JsonProperty("id")
            val id: Id,

            @JsonProperty("title")
            val title: String,

            @JsonProperty("total_events")
            val totalEvents: Long,

            @JsonProperty("percent")
            val percent: Double,
        )
    }
}
