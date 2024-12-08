package com.lamion.feature.logger.payload

import com.lamion.server.validation.NullOrNotBlank
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LogRequest(
    val events: List<Event>?,

    val errors: List<Error>?,

    @field:NotNull
    val user: User,

    @field:NotNull
    val device: Device,
) {
    data class Event(
        @field:NotBlank
        val function: String,

        @field:NullOrNotBlank
        val feature: String?,

        val createdAt: Long,
    )

    data class Error(
        @field:NotBlank
        val function: String?,
        val createdAt: Long,
        val text: String,
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
