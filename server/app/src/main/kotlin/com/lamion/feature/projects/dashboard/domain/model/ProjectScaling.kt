package com.lamion.feature.projects.dashboard.domain.model

import com.lamion.domain.model.ComparisonDomain

data class ProjectScaling(
    val totalUsers: ComparisonDomain<Long>,
    val activeUsers: ComparisonDomain<Long>,
    val totalCrashes: ComparisonDomain<Long>,
    val triggeredEvents: ComparisonDomain<Long>,
)