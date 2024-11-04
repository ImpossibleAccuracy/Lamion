package com.application.lamion.feature.shared.payload

import com.application.lamion.domain.model.Id
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface FeatureDto {
    val id: Id
    val title: String
    val description: String?

    @Serializable
    data class Partial(
        @SerialName("id")
        override val id: Id,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String?,
    ) : FeatureDto

    @Serializable
    data class WithEvents(
        @SerialName("id")
        override val id: Id,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String?,

        @SerialName("total_events")
        val totalEvents: Long,

        @SerialName("total_events_percent")
        val totalEventsPercent: Double,
    ) : FeatureDto

    @Serializable
    data class Detailed(
        @SerialName("id")
        override val id: Id,

        @SerialName("title")
        override val title: String,

        @SerialName("description")
        override val description: String?,

        @SerialName("functions_count")
        val functionsCount: Long,

        @SerialName("total_events")
        val totalEvents: Long,

        @SerialName("total_errors")
        val totalErrors: Long,

        @SerialName("top_function")
        val topFunction: List<TopFunction>,
    ) : FeatureDto {
        @Serializable
        data class TopFunction(
            @SerialName("id")
            val id: Id,

            @SerialName("title")
            val title: String,

            @SerialName("total_events")
            val totalEvents: Long,

            @SerialName("percent")
            val percent: Double,
        )
    }
}
