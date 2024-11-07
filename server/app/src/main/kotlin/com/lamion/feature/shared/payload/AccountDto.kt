package com.lamion.feature.shared.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.domain.model.Id

sealed interface AccountDto {
    val id: Id
    val username: String
    val avatar: String?

    data class Public(
        @JsonProperty("id")
        override val id: Id,

        @JsonProperty("username")
        override val username: String,

        @JsonProperty("avatar")
        override val avatar: String?,
    ) : AccountDto

    data class Total(
        @JsonProperty("id")
        override val id: Id,

        @JsonProperty("username")
        override val username: String,

        @JsonProperty("email")
        val email: String,

        @JsonProperty("avatar")
        override val avatar: String?,
    ) : AccountDto
}
