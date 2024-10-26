package com.application.lamion.server.security

import com.application.lamion.domain.exception.OperationDeniedException
import com.application.lamion.domain.model.AccountDomain
import com.application.lamion.domain.security.Authorization
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext

object UserHolder {
    private suspend fun currentUser(): Authorization? =
        ReactiveSecurityContextHolder.getContext()
            .mapNotNull<Authorization> { ctx: SecurityContext ->
                try {
                    (ctx.authentication.principal as Authorization)
                } catch (e: ClassCastException) {
                    null
                } catch (e: NullPointerException) {
                    null
                }
            }
            .awaitSingleOrNull()

    suspend fun requireAccount(): AccountDomain.Total = currentUser()?.account
        ?: throw OperationDeniedException("No authorization found")
}