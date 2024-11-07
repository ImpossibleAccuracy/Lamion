package com.lamion.feature.projects.feature.controller.payload.response

import com.lamion.feature.shared.payload.ChartDto
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeaturesResponse(
    @SerialName("total_events")
    val events: ChartDto<LocalDate, Long>,

    @SerialName("total_features")
    val totalFeatures: Long,
)
