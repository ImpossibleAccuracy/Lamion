package com.application.lamion.feature.projects.feature.domain.model

import com.application.lamion.domain.model.ChartDomain

data class TopFeatures(
    val items: ChartDomain<FeatureDomain.Partial, Long>,
    val totalEvents: Long,
    val avgEventsPerDay: Long,
)
