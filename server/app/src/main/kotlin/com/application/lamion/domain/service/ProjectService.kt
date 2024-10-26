package com.application.lamion.domain.service

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain

interface ProjectService {
    suspend fun get(id: Id, account: AccountDomain): ProjectDomain?

    suspend fun require(id: Id, account: AccountDomain): ProjectDomain
}