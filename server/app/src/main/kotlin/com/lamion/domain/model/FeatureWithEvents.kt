package com.lamion.domain.model

data class FeatureWithEvents(
    val id: Id,
    val title: String,
    val description: String?,
    val totalEvents: Long,
    val totalEventsPercent: Double,
)