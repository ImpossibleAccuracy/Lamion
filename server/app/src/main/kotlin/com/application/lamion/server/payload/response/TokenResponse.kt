package com.application.lamion.server.payload.response

import com.application.lamion.server.payload.dto.UserDto

data class TokenResponse(
    var user: UserDto,
    var token: String,
)
