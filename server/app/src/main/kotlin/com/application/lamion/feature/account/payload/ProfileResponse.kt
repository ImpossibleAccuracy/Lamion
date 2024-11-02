package com.application.lamion.feature.account.payload

import com.application.lamion.feature.shared.payload.dto.AccountDto
import com.fasterxml.jackson.annotation.JsonProperty

data class ProfileResponse(
    @field:JsonProperty("account")
    val account: AccountDto.Total,
)
