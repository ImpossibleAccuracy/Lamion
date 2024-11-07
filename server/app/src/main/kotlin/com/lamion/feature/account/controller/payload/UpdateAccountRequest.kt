package com.lamion.feature.account.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.server.validation.NullOrNotBlank

data class UpdateAccountRequest(
    @field:NullOrNotBlank
    @JsonProperty("email")
    val email: String,

    @field:NullOrNotBlank
    @JsonProperty("username")
    val username: String,
)
