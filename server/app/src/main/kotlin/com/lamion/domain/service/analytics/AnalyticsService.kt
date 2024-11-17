package com.lamion.domain.service.analytics

import com.lamion.domain.model.ComparisonDomain
import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain

interface AnalyticsService {
    suspend fun getTotalUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun getActiveUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun getTotalEvents(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun getTotalErrors(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>
}