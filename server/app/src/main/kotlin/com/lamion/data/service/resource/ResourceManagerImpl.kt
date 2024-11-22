package com.lamion.data.service.resource

import com.lamion.data.properties.ResourcesProperties
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.service.resource.ResourceManager
import org.springframework.stereotype.Service

@Service
class ResourceManagerImpl(
    private val resourcesProperties: ResourcesProperties,
) : ResourceManager {
    override suspend fun getAvatarUrl(account: AccountDomain): String? {
        if (account.avatar == null) return null

        return "${resourcesProperties.resourcesUrl}${account.id}"
    }
}