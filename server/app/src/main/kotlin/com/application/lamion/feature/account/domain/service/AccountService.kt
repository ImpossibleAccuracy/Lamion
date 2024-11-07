package com.application.lamion.feature.account.domain.service

import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.file.LocalFile
import org.springframework.http.codec.multipart.FilePart

interface AccountService {
    suspend fun getAccount(
        caller: AccountDomain,
        target: Id,
    ): AccountDomain?

    suspend fun updateAccount(
        account: AccountDomain,
        email: String?,
        username: String?,
    ): AccountDomain.Total

    suspend fun getAvatar(account: AccountDomain): LocalFile?

    suspend fun updateAvatar(
        account: AccountDomain,
        file: FilePart,
    )
}