package com.application.lamion.data.database.mapper

import com.application.lamion.data.database.entity.AccountEntity
import com.application.lamion.domain.model.AccountDomain

fun AccountEntity.toDomain() = AccountDomain.Total(
    id = id,
    username = username,
    email = email,
    avatar = null // TODO
)