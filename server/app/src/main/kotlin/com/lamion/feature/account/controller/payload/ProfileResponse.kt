package com.lamion.feature.account.controller.payload

import com.lamion.feature.shared.payload.AccountDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("account")
    val account: AccountDto.Total,
)
