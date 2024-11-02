package com.application.lamion.feature.auth.controller.payload.response

import com.application.lamion.feature.shared.payload.dto.AccountDto
import com.fasterxml.jackson.annotation.JsonProperty

data class AuthResponse(
    @field:JsonProperty("account")
    val account: AccountDto.Public,

    @field:JsonProperty("token")
    val token: String,
)
