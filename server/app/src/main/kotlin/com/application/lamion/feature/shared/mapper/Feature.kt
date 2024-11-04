package com.application.lamion.feature.shared.mapper

import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.feature.shared.payload.FeatureDto

fun FeatureWithEvents.toDto() = FeatureDto.WithEvents(
    id = id,
    title = title,
    description = description,
    totalEvents = totalEvents,
    totalEventsPercent = totalEventsPercent,
)