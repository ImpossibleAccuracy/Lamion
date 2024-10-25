package com.application.lamion.domain.model

data class Authorization(
    val account: AccountDomain,
    val roles: List<AccountRole>,
)
