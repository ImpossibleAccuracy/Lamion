package com.application.lamion.domain.model

data class AuthResult(
    val user: AccountDomain,
    val token: String,
)
