package com.lamion.feature.shared.mapper

import com.lamion.domain.model.FeatureWithEvents
import com.lamion.feature.shared.payload.FeatureDto

fun FeatureWithEvents.toDto() = FeatureDto.WithEvents(
    id = id,
    title = title,
    description = description,
    totalEvents = totalEvents,
    totalEventsPercent = totalEventsPercent,
)