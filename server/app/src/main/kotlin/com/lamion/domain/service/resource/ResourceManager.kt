package com.lamion.domain.service.resource

import com.lamion.domain.model.AccountDomain

interface ResourceManager {
    suspend fun getAvatarUrl(account: AccountDomain): String?
}