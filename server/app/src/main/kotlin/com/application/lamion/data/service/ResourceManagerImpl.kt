package com.application.lamion.data.service

import com.application.lamion.data.properties.ResourcesProperties
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.service.ResourceManager
import org.springframework.stereotype.Service

@Service
class ResourceManagerImpl(
    private val resourcesProperties: ResourcesProperties,
) : ResourceManager {
    override suspend fun getAvatarUrl(account: AccountDomain): String {
        return "${resourcesProperties.resourcesUrl}/${account.id}"
    }
}