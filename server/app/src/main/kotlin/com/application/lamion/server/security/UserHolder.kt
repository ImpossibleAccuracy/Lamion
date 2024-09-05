package com.application.lamion.server.security

import com.application.lamion.domain.exception.OperationDeniedException
import com.application.lamion.domain.model.UserDomain
import org.springframework.security.core.context.SecurityContextHolder

object UserHolder {
    private val currentUser: UserDomain?
        get() = SecurityContextHolder.getContext().authentication.principal as? UserDomain

    fun getUserOrThrow(): UserDomain = currentUser
        ?: throw OperationDeniedException("No authorization found")
}