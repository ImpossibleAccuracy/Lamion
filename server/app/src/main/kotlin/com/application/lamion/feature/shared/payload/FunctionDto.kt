package com.application.lamion.feature.shared.payload

import com.application.lamion.domain.model.Id
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface FunctionDto {
    val id: Id
    val title: String

    @Serializable
    data class Partial(
        @SerialName("ud")
        override val id: Id,

        @SerialName("title")
        override val title: String,
    ) : FunctionDto

    @Serializable
    data class Detailed(
        @SerialName("id")
        override val id: Id,

        @SerialName("title")
        override val title: String,

        @SerialName("total_events")
        val totalEvents: Long,

        @SerialName("features")
        val features: List<FeatureDto>,

        @SerialName("tags")
        val tags: List<Tag>,
    ) : FunctionDto {
        @Serializable
        data class Tag(
            @SerialName("id")
            val id: Id,

            @SerialName("title")
            val title: String,
        )
    }
}
