package com.application.lamion.feature.auth.controller.payload.response

import com.application.lamion.feature.shared.payload.dto.AccountDto

data class AuthResponse(
    val account: AccountDto.Public,
    val token: String,
)
