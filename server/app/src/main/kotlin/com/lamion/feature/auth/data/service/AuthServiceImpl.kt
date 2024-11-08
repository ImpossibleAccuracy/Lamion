package com.lamion.feature.auth.data.service

import com.lamion.data.database.table.AccountTable
import com.lamion.data.database.table.RoleTable
import com.lamion.data.service.token.TokenService
import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.domain.exception.UnauthorizedException
import com.lamion.domain.model.AccountDomain
import com.lamion.domain.security.AccountRole
import com.lamion.domain.security.Authorization
import com.lamion.feature.account.data.mapper.toAccountDomain
import com.lamion.feature.auth.data.datasource.AccountDataSource
import com.lamion.feature.auth.domain.model.AuthResult
import com.lamion.feature.auth.domain.service.AuthService
import com.lamion.feature.shared.utils.require
import com.lamion.utils.dbQuery
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

        val account = AccountDataSource
            .findAccount(subject)
            .require { "Account not found" }
            .toAccountDomain()

        val roles = AccountDataSource
            .findAccountRoles(account.id)
            .map {
                AccountRole.valueOf(it[RoleTable.title])
            }

        Authorization(account, roles)
    }

    override suspend fun signIn(email: String, password: String): AuthResult = dbQuery {
        AccountDataSource
            .findAccountByEmail(email)
            ?.takeIf {
                val passwordHash = it[AccountTable.password]

                passwordHash != null && passwordEncoder.matches(password, passwordHash)
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

    override suspend fun signUp(
        username: String,
        email: String,
        password: String?
    ): AuthResult = dbQuery {
        AccountDataSource
            .existsAccountByEmail(email)
            .let {
                if (it) {
                    throw InvalidArgumentsException("User with such credentials already exists")
                }
            }

        AccountDataSource
            .createAccount(
                username = username,
                email = email,
                password = password?.let {
                    passwordEncoder.encode(password)
                }
            )
            .toAccountDomain()
            .let {
                AuthResult(
                    user = it,
                    token = generateToken(it)
                )
            }
    }

    override suspend fun createUserWithOauth(
        username: String?,
        email: String,
    ): AuthResult {
        dbQuery { AccountDataSource.findAccountByEmail(email) }
            ?.toAccountDomain()
            ?.let {
                return AuthResult(
                    user = it,
                    token = generateToken(it)
                )
            }

        return signUp(
            username = username ?: email,
            email = email,
            password = null,
        )
    }

    private suspend inline fun generateToken(account: AccountDomain): String =
        tokenService.generateToken(account.id.toString())
}
