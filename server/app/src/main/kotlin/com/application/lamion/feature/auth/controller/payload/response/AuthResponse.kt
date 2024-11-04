package com.application.lamion.feature.auth.controller.payload.response

import com.application.lamion.feature.shared.payload.AccountDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("account")
    val account: AccountDto.Public,

    @SerialName("token")
    val token: String,
)
