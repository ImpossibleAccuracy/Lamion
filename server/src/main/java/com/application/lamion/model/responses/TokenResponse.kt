package com.application.lamion.model.responses

import com.application.lamion.model.User

data class TokenResponse(
    var user: User,
    var token: String,
)
