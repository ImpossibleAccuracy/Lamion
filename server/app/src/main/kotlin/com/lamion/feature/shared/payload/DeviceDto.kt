package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty

sealed interface DeviceDto {
    val title: String
    val activity: ComparisonDto<Long>
    val platform: String

    data class Partial(
        @JsonProperty("title")
        override val title: String,

        @JsonProperty("activity")
        override val activity: ComparisonDto<Long>,

        @JsonProperty("platform")
        override val platform: String
    ) : DeviceDto

    data class Detailed(
        @JsonProperty("title")
        override val title: String,

        @JsonProperty("platform")
        override val platform: String,

        @JsonProperty("activity")
        override val activity: ComparisonDto<Long>,

        @JsonProperty("errors")
        val errors: ComparisonDto<Long>,
    ) : DeviceDto
}
