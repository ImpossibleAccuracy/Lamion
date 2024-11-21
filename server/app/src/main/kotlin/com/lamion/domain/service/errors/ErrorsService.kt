package com.lamion.domain.service.errors

import com.lamion.domain.model.*
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import kotlinx.datetime.LocalDate

interface ErrorsService {
    suspend fun countTotalErrors(
        project: ProjectDomain,
        dateRange: DateRange
    ): Long

    suspend fun getTotalErrorsComparison(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun getTotalErrorsComparison(
        feature: FeatureDomain,
        dateRange: ExtendedDateRange,
    ): ComparisonDomain<Long>

    suspend fun countErrorsGroupByDateByFeature(
        feature: FeatureDomain,
        dateRange: DateRange,
    ): ChartDomain<LocalDate, Long>
}