package com.lamion.feature.account.domain.service

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.domain.service.file.LocalFile
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

    suspend fun getAvatar(accountId: Id): LocalFile?

    suspend fun updateAvatar(
        account: AccountDomain,
        file: FilePart,
    )

    suspend fun deleteAvatar(account: AccountDomain)
}