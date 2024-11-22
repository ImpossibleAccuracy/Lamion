package com.lamion.data.service.error

import com.lamion.domain.model.*
import com.lamion.domain.service.errors.ErrorsService
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.datetime.LocalDate
import org.springframework.stereotype.Service

@Service
class ErrorsServiceImpl : ErrorsService {
    override suspend fun countTotalErrors(project: ProjectDomain, dateRange: DateRange): Long = dbQuery {
        ErrorDataSource.countErrorsByProjectId(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun getTotalErrorsComparison(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> = ComparisonDomain.fromDateRange(dateRange) {
        asyncDbQuery {
            ErrorDataSource.countErrorsByProjectId(
                projectId = project.id,
                start = it.start,
                end = it.end,
            )
        }
    }

    override suspend fun getTotalErrorsComparison(
        feature: FeatureDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> = ComparisonDomain.fromDateRange(dateRange) {
        asyncDbQuery {
            ErrorDataSource.countErrorsByFeatureId(
                featureId = feature.id,
                start = it.start,
                end = it.end,
            )
        }
    }

    override suspend fun countErrorsGroupByDateByFeature(
        feature: FeatureDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        ErrorDataSource.getErrorsByFeatureGroupByDate(
            featureId = feature.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }
}