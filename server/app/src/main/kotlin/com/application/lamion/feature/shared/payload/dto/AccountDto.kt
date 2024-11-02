package com.application.lamion.feature.shared.payload.dto

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty

sealed interface AccountDto {
    val id: Id
    val username: String
    val avatar: String?

    data class Public(
        @field:JsonProperty("id")
        override val id: Id,

        @field:JsonProperty("username")
        override val username: String,

        @field:JsonProperty("avatar")
        override val avatar: String?,
    ) : AccountDto

    data class Total(
        @field:JsonProperty("id")
        override val id: Id,

        @field:JsonProperty("username")
        override val username: String,

        @field:JsonProperty("email")
        val email: String,

        @field:JsonProperty("avatar")
        override val avatar: String?,
    ) : AccountDto
}
