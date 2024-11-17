package com.lamion.data.service.analytics

import com.lamion.data.datasource.ErrorDataSource
import com.lamion.data.datasource.EventDataSource
import com.lamion.data.datasource.UserDataSource
import com.lamion.domain.model.ComparisonDomain
import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.analytics.AnalyticsService
import com.lamion.utils.asyncDbQuery
import org.springframework.stereotype.Service

@Service
class AnalyticsServiceImpl : AnalyticsService {
    override suspend fun getTotalUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> {
        val actual = asyncDbQuery {
            UserDataSource.getUsersCount(
                projectId = project.id,
                start = dateRange.second.start,
                end = dateRange.second.end,
            )
        }

        val past = asyncDbQuery {
            UserDataSource.getUsersCount(
                projectId = project.id,
                start = dateRange.first.start,
                end = dateRange.first.end,
            )
        }

        return ComparisonDomain(
            actual = actual.await(),
            past = past.await(),
        )
    }

    override suspend fun getActiveUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> {
        val actual = asyncDbQuery {
            UserDataSource.getActiveUsersCount(
                projectId = project.id,
                start = dateRange.second.start,
                end = dateRange.second.end,
            )
        }

        val past = asyncDbQuery {
            UserDataSource.getActiveUsersCount(
                projectId = project.id,
                start = dateRange.first.start,
                end = dateRange.first.end,
            )
        }

        return ComparisonDomain(
            actual = actual.await(),
            past = past.await(),
        )
    }

    override suspend fun getTotalEvents(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> {
        val actual = asyncDbQuery {
            ErrorDataSource.countErrorsByCreatedBetween(
                projectId = project.id,
                start = dateRange.second.start,
                end = dateRange.second.end,
            )
        }

        val past = asyncDbQuery {
            ErrorDataSource.countErrorsByCreatedBetween(
                projectId = project.id,
                start = dateRange.first.start,
                end = dateRange.first.end,
            )
        }

        return ComparisonDomain(
            actual = actual.await(),
            past = past.await(),
        )
    }

    override suspend fun getTotalErrors(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long> {
        val actual = asyncDbQuery {
            EventDataSource.countEventsByCreatedBetween(
                projectId = project.id,
                start = dateRange.second.start,
                end = dateRange.second.end,
            )
        }

        val past = asyncDbQuery {
            EventDataSource.countEventsByCreatedBetween(
                projectId = project.id,
                start = dateRange.first.start,
                end = dateRange.first.end,
            )
        }

        return ComparisonDomain(
            actual = actual.await(),
            past = past.await(),
        )
    }
}