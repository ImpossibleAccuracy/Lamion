package com.application.lamion.domain.security

import com.application.lamion.domain.model.AccountDomain

data class Authorization(
    val account: AccountDomain.Total,
    val roles: List<AccountRole>,
)
