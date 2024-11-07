package com.lamion.feature.account.controller.payload

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.AccountDto

data class ProfileResponse(
    @JsonProperty("account")
    val account: AccountDto.Total,
)
