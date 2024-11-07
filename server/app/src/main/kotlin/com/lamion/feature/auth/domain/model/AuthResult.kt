package com.lamion.feature.auth.domain.model

import com.lamion.domain.model.AccountDomain

data class AuthResult(
    val user: AccountDomain,
    val token: String,
)