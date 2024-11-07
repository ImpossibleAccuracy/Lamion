package com.lamion.domain.service.project

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.model.ProjectDomain

interface ProjectService {
    suspend fun get(id: Id, account: AccountDomain): ProjectDomain?

    suspend fun require(id: Id, account: AccountDomain): ProjectDomain
}