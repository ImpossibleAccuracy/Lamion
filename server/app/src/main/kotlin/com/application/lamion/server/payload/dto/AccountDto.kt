package com.application.lamion.server.payload.dto

import com.application.lamion.domain.model.Id
import com.fasterxml.jackson.annotation.JsonProperty

data class AccountDto(
    @JsonProperty("id")
    val id: Id,

    @JsonProperty("username")
    val username: String,
)
