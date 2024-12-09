package com.lamion.domain.service.analytics

import com.lamion.domain.model.ComparisonDomain
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.model.TimePeriod

interface AnalyticsService {
    suspend fun computeGrowthRate(
        project: ProjectDomain,
        timePeriod: TimePeriod,
    ): ComparisonDomain<Double>?
}