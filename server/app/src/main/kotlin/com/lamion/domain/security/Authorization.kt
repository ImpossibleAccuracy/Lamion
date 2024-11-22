package com.lamion.domain.security

import com.lamion.domain.model.AccountDomain

data class Authorization(
    val account: AccountDomain.Total,
    val roles: List<AccountRole>,
)
