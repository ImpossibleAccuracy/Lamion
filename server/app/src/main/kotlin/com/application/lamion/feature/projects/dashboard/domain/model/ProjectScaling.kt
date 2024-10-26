package com.application.lamion.feature.projects.dashboard.domain.model

import com.application.lamion.domain.model.ComparisonDomain

data class ProjectScaling(
    val totalUsers: ComparisonDomain<Long>,
    val activeUsers: ComparisonDomain<Long>,
    val totalCrashes: ComparisonDomain<Long>,
    val triggeredEvents: ComparisonDomain<Long>,
)