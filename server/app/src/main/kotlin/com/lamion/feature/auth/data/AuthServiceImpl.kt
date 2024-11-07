package com.lamion.feature.auth.data

import com.lamion.data.database.table.AccountTable
import com.lamion.data.database.table.RoleTable
import com.lamion.data.database.table.refs.AccountRoleRef
import com.lamion.data.database.utils.exists
import com.lamion.data.database.utils.new
import com.lamion.data.service.token.TokenService
import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.domain.exception.UnauthorizedException
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.security.AccountRole
import com.lamion.domain.security.Authorization
import com.lamion.feature.account.data.mapper.toAccountDomain
import com.lamion.feature.auth.domain.model.AuthResult
import com.lamion.feature.auth.domain.service.AuthService
import com.lamion.feature.shared.utils.require
import com.lamion.utils.dbQuery
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl @Autowired constructor(
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {
    override suspend fun authUser(token: String): Authorization = dbQuery {
        val subject = tokenService.extractSubject(token)?.toLongOrNull()
            ?: throw UnauthorizedException("Token invalid or expired")

        val account = AccountTable
            .selectAll()
            .where { AccountTable.id eq subject }
            .firstOrNull()
            .require { "Account not found" }
            .toAccountDomain()

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

    override suspend fun signIn(email: String, password: String): AuthResult = dbQuery {
        AccountTable
            .selectAll()
            .where { AccountTable.email eq email }
            .firstOrNull()
            ?.takeIf {
                passwordEncoder.matches(password, it[AccountTable.password])
            }
            .require { "User with such credentials not found" }
            .toAccountDomain()
            .let {
                AuthResult(
                    user = it,
                    token = generateToken(it)
                )
            }
    }

    override suspend fun signUp(username: String, email: String, password: String): AuthResult =
        dbQuery {
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
                .toAccountDomain()
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
