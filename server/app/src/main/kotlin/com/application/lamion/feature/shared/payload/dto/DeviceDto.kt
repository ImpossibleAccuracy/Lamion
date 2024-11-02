package com.application.lamion.feature.shared.payload.dto

import com.fasterxml.jackson.annotation.JsonProperty

sealed interface DeviceDto {
    val title: String
    val activity: ComparisonDto<Long>
    val platform: String

    data class Partial(
        @field:JsonProperty("title")
        override val title: String,

        @field:JsonProperty("activity")
        override val activity: ComparisonDto<Long>,

        @field:JsonProperty("platform")
        override val platform: String
    ) : DeviceDto

    data class Detailed(
        @field:JsonProperty("title")
        override val title: String,

        @field:JsonProperty("platform")
        override val platform: String,

        @field:JsonProperty("activity")
        override val activity: ComparisonDto<Long>,

        @field:JsonProperty("errors")
        val errors: ComparisonDto<Long>,
    ) : DeviceDto
}
