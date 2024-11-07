package com.application.lamion.feature.shared.mapper

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.service.resource.ResourceManager
import com.application.lamion.feature.shared.payload.AccountDto

suspend fun AccountDomain.toPublicDto(resourceManager: ResourceManager) =
    AccountDto.Public(
        id = id,
        username = username,
        avatar = resourceManager.getAvatarUrl(this),
    )

suspend fun AccountDomain.Total.toTotalDto(resourceManager: ResourceManager) =
    AccountDto.Total(
        id = id,
        username = username,
        email = email,
        avatar = resourceManager.getAvatarUrl(this),
    )
