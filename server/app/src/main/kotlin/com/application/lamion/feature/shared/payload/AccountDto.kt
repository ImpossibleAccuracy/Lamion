package com.application.lamion.feature.shared.payload

import com.application.lamion.domain.model.Id
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AccountDto {
    val id: Id
    val username: String
    val avatar: String?

    @Serializable
    data class Public(
        @SerialName("id")
        override val id: Id,

        @SerialName("username")
        override val username: String,

        @SerialName("avatar")
        override val avatar: String?,
    ) : AccountDto

    @Serializable
    data class Total(
        @SerialName("id")
        override val id: Id,

        @SerialName("username")
        override val username: String,

        @SerialName("email")
        val email: String,

        @SerialName("avatar")
        override val avatar: String?,
    ) : AccountDto
}
