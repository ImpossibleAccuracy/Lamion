package com.lamion.feature.logger.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class LogEventsRequest(
    @field:Size(min = 1)
    val events: List<Event>,

    @field:NotNull
    val user: User,

    @field:NotNull
    val device: Device,
) {
    data class Event(
        @field:NotBlank
        val function: String,

        val createdAt: LocalDateTime,
    )

    data class Device(
        @field:NotBlank
        val name: String,

        @field:NotBlank
        val platform: String,
    )

    data class User(
        val deviceKey: String?,
        val clientKey: String?,
    )
}
