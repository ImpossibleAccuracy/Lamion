package com.lamion.feature.auth.controller.payload.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id

data class AuthResponse(
    @JsonProperty("id")
    val id: Id,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("avatar")
    val avatar: String?,

    @JsonProperty("refreshToken")
    val token: String,
)
