package com.lamion.feature.projects.feature.controller.payload.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.feature.shared.payload.ProgressDto
import java.time.LocalDate

data class FeaturesResponse(
    @JsonProperty("total_events")
    val events: ProgressDto<LocalDate, Long>,

    @JsonProperty("total_features")
    val totalFeatures: Long,
)
