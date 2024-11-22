package com.lamion.feature.projects.settings.domain.service

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.projects.settings.domain.model.AccessKeyDomain
import com.lamion.feature.projects.settings.domain.model.AccessKeyWithValueDomain

interface ProjectAccessKeyService {
    suspend fun create(
        account: AccountDomain,
        project: ProjectDomain,
        title: String,
    ): AccessKeyWithValueDomain

    suspend fun get(
        account: AccountDomain,
        project: ProjectDomain,
        keyId: Id,
    ): AccessKeyDomain

    suspend fun getAccessKeys(
        account: AccountDomain,
        project: ProjectDomain
    ): List<AccessKeyDomain>

    suspend fun delete(
        account: AccountDomain,
        target: AccessKeyDomain,
    )

    suspend fun deleteAll(
        account: AccountDomain,
        project: ProjectDomain,
    )
}
