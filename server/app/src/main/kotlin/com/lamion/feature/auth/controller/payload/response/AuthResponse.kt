package com.lamion.feature.auth.controller.payload.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.AccountDto

data class AuthResponse(
    @JsonProperty("account")
    val account: AccountDto.Public,

    @JsonProperty("token")
    val token: String,
)
