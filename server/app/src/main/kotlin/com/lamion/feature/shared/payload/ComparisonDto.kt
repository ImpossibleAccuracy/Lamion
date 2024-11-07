package com.lamion.feature.shared.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComparisonDto<T>(
    @SerialName("actual")
    val actual: T,

    @SerialName("past")
    val past: T,
)