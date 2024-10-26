package com.application.lamion.feature.auth.domain.model

import com.application.lamion.domain.model.AccountDomain

data class AuthResult(
    val user: AccountDomain,
    val token: String,
)