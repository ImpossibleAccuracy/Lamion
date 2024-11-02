package com.application.lamion.feature.shared.payload.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface DeviceDto {
    val title: String
    val activity: ComparisonDto<Long>
    val platform: String

    @Serializable
    data class Partial(
        @SerialName("title")
        override val title: String,

        @SerialName("activity")
        override val activity: ComparisonDto<Long>,

        @SerialName("platform")
        override val platform: String
    ) : DeviceDto

    @Serializable
    data class Detailed(
        @SerialName("title")
        override val title: String,

        @SerialName("platform")
        override val platform: String,

        @SerialName("activity")
        override val activity: ComparisonDto<Long>,

        @SerialName("errors")
        val errors: ComparisonDto<Long>,
    ) : DeviceDto
}
