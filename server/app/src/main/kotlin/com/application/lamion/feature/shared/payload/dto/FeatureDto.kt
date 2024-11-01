package com.application.lamion.feature.shared.payload.dto

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty

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
        val totalEventsPercent: Long,
    ) : FeatureDto

    data class Detailed(
        override val id: Id,
        override val title: String,
        override val description: String?,
        val functionsCount: Long,
        val totalEvents: Long,
        val totalErrors: Long,
        val topFunction: List<TopFunction>,
    ) : FeatureDto {
        data class TopFunction(
            val id: Id,
            val title: String,
            val totalEvents: Long,
            val percent: Double,
        )
    }
}
