package com.application.lamion.data.service

import com.application.lamion.data.database.entity.AccountEntity
import com.application.lamion.data.database.repository.AccountRepository
import com.application.lamion.data.database.repository.AccountRoleRepository
import com.application.lamion.domain.exception.InvalidArgumentsException
import com.application.lamion.domain.exception.ResourceNotFoundException
import com.application.lamion.domain.exception.UnauthorizedException
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.model.AuthResult
import com.application.lamion.domain.model.Authorization
import com.application.lamion.domain.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl @Autowired constructor(
    private val accountRepository: AccountRepository,
    private val accountRoleRepository: AccountRoleRepository,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {
    override suspend fun authUser(token: String): Authorization {
        val subject = tokenService.extractSubject(token)?.toLongOrNull()
            ?: throw UnauthorizedException("Token invalid or expired")

        val account = accountRepository.findById(subject).awaitSingleOrNull()
            ?: throw ResourceNotFoundException("Account not found")

        val roles = accountRoleRepository
            .findByAccountId(account.id)
            .asFlow()
            .map { it.enum }
            .toList()

        return Authorization(account, roles)
    }

    override suspend fun signIn(email: String, password: String): AuthResult = accountRepository
        .findFirstByEmail(email)
        .awaitSingleOrNull()
        ?.takeIf {
            passwordEncoder.matches(password, it.password)
        }
        ?.let {
            AuthResult(
                user = it,
                token = generateToken(it)
            )
        }
        ?: throw ResourceNotFoundException("User with such credentials not found")

    override suspend fun signUp(username: String, email: String, password: String): AuthResult {
        if (accountRepository.existsByEmail(email).awaitSingle()) {
            throw InvalidArgumentsException("User with such credentials already exists")
        }

        return AccountEntity(
            username = username,
            email = email,
            password = passwordEncoder.encode(password)
        )
            .let {
                accountRepository.save(it).awaitSingle()
            }
            .let {
                AuthResult(
                    user = it,
                    token = generateToken(it)
                )
            }
    }

    private suspend fun generateToken(account: AccountDomain): String = withContext(Dispatchers.Default) {
        tokenService.generateToken(account.id.toString())
    }
}
