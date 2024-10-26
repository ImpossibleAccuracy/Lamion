package com.application.lamion.domain.model

sealed interface AccountDomain {
    val id: Id
    val username: String
    val avatar: Id?

    data class Public(
        override val id: Id,
        override val username: String,
        override val avatar: Id?,
    ) : AccountDomain

    data class Total(
        override val id: Id,
        override val username: String,
        val email: String,
        override val avatar: Id?,
    ) : AccountDomain
}
