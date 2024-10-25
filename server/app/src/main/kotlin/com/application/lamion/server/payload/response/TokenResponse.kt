package com.application.lamion.server.payload.response

import com.application.lamion.server.payload.dto.AccountDto

data class TokenResponse(
    var user: AccountDto,
    var token: String,
)
