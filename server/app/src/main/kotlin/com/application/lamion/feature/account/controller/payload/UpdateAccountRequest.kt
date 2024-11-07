package com.application.lamion.feature.account.controller.payload

import com.application.lamion.server.validation.NullOrNotBlank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountRequest(
    @field:NullOrNotBlank
    @SerialName("email")
    val email: String,

    @field:NullOrNotBlank
    @SerialName("username")
    val username: String,
)
