package com.lamion.feature.logger.payload

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class LogEventsBulkRequest(
    @field:Size(min = 1)
    val events: List<Event>,

    @field:NotNull
    val user: User,

    @field:NotNull
    val device: Device,
) {
    @Serializable
    data class Event(
        @field:NotBlank
        val function: String,

        val createdAt: LocalDateTime,
    )

    @Serializable
    data class Device(
        @field:NotBlank
        val name: String,

        @field:NotBlank
        val platform: String,
    )

    @Serializable
    data class User(
        val deviceKey: String?,
        val clientKey: String?,
    )
}
