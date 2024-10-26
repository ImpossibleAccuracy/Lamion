package com.application.lamion.feature.account.payload

import com.application.lamion.feature.shared.payload.dto.AccountDto

data class ProfileResponse(
    val account: AccountDto.Total,
)
