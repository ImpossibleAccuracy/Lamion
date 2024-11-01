package com.application.lamion.feature.auth.data

import com.application.lamion.data.database.table.AccountTable
import com.application.lamion.data.database.table.RoleTable
import com.application.lamion.data.database.table.refs.AccountRoleRef
import com.application.lamion.data.database.utils.exists
import com.application.lamion.data.database.utils.new
import com.application.lamion.data.service.TokenService
import com.application.lamion.domain.exception.InvalidArgumentsException
import com.application.lamion.domain.exception.UnauthorizedException
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.security.AccountRole
import com.application.lamion.domain.security.Authorization
import com.application.lamion.feature.auth.domain.model.AuthResult
import com.application.lamion.feature.auth.domain.service.AuthService
import com.application.lamion.feature.shared.utils.require
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl @Autowired constructor(
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {
    override suspend fun authUser(token: String): Authorization = newSuspendedTransaction(Dispatchers.IO) {
        val subject = tokenService.extractSubject(token)?.toLongOrNull()
            ?: throw UnauthorizedException("Token invalid or expired")

        val account = AccountTable
            .selectAll()
            .where { AccountTable.id eq subject }
            .firstOrNull()
            .require { "Account not found" }
            .toDomain()

        val roles = RoleTable
            .join(AccountRoleRef, JoinType.INNER)
            .selectAll()
            .where(AccountRoleRef.account eq account.id)
            .toList()
            .map {
                AccountRole.valueOf(it[RoleTable.title])
            }

        Authorization(account, roles)
    }

    override suspend fun signIn(email: String, password: String): AuthResult = newSuspendedTransaction(Dispatchers.IO) {
        AccountTable
            .selectAll()
            .where { AccountTable.email eq email }
            .firstOrNull()
            ?.takeIf {
                passwordEncoder.matches(password, it[AccountTable.password])
            }
            .require { "User with such credentials not found" }
            .toDomain()
            .let {
                AuthResult(
                    user = it,
                    token = generateToken(it)
                )
            }
    }

    override suspend fun signUp(username: String, email: String, password: String): AuthResult =
        newSuspendedTransaction(Dispatchers.IO) {
            AccountTable
                .select(AccountTable.id)
                .where { AccountTable.email eq email }
                .exists()
                .let {
                    if (it) {
                        throw InvalidArgumentsException("User with such credentials already exists")
                    }
                }

            AccountTable
                .new {
                    it[AccountTable.username] = username
                    it[AccountTable.email] = email
                    it[AccountTable.password] = passwordEncoder.encode(password)
                }!!
                .toDomain()
                .let {
                    AuthResult(
                        user = it,
                        token = generateToken(it)
                    )
                }
        }

    private suspend inline fun generateToken(account: AccountDomain): String =
        tokenService.generateToken(account.id.toString())
}

private fun ResultRow.toDomain() = AccountDomain.Total(
    id = this[AccountTable.id].value,
    username = this[AccountTable.username],
    email = this[AccountTable.email],
    avatar = this[AccountTable.avatar]?.value,
)
