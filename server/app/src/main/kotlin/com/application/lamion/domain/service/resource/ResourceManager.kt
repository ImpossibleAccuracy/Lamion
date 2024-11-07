package com.application.lamion.domain.service.resource

import com.application.lamion.domain.model.AccountDomain

interface ResourceManager {
    suspend fun getAvatarUrl(account: AccountDomain): String?
}