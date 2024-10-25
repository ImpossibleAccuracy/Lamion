package com.application.lamion.server.mapper

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.server.payload.dto.AccountDto

fun AccountDomain.toDto() = AccountDto(
    id = id,
    username = username
)