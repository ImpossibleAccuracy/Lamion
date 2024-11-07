package com.lamion.feature.account.data.service

import com.lamion.data.database.table.AccountTable
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.feature.account.data.mapper.toAccountDomain
import com.lamion.feature.account.domain.service.AccountService
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.updateReturning
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AccountServiceImpl(
    private val fileStorageService: com.lamion.domain.service.file.FileStorageService,
) : AccountService {
    companion object {
        private val AllowedMimeTypes = arrayOf(
            MediaType.IMAGE_JPEG,
            MediaType.IMAGE_PNG,
        )
    }

    override suspend fun getAccount(caller: AccountDomain, target: Id): AccountDomain? = dbQuery {
        AccountTable.selectAll()
            .where(AccountTable.id.eq(target))
            .firstOrNull()
            ?.toAccountDomain()
    }

    override suspend fun updateAccount(
        account: AccountDomain,
        email: String?,
        username: String?
    ): AccountDomain.Total = dbQuery {
        AccountTable
            .updateReturning(where = { AccountTable.id eq account.id }) {
                if (email != null) {
                    it[AccountTable.email] = email
                }

                if (username != null) {
                    it[AccountTable.username] = username
                }
            }
            .first()
            .toAccountDomain()
    }

    override suspend fun getAvatar(account: AccountDomain): com.lamion.domain.service.file.LocalFile? =
        account.avatar?.let { avatarId ->
            fileStorageService.getFileById(avatarId)
        }

    override suspend fun updateAvatar(account: AccountDomain, file: FilePart) {
        assertFileIsAcceptable(file)

        val avatar = fileStorageService.findFileByHashOrCreate(file)

        dbQuery {
            AccountTable.update(where = { AccountTable.id eq account.id }) {
                it[AccountTable.avatar] = avatar.id
            }
        }
    }

    private fun assertFileIsAcceptable(file: FilePart) {
        val headers = file.headers()

        if (headers.contentLength == 0L) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty")
        }

        if (headers.contentType == null || headers.contentType!! !in AllowedMimeTypes) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File of unacceptable type")
        }
    }
}
