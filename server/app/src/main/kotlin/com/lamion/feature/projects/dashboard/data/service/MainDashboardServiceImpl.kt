package com.lamion.feature.projects.dashboard.data.service

import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.analytics.AnalyticsService
import com.lamion.feature.projects.dashboard.domain.model.ProjectScaling
import com.lamion.feature.projects.dashboard.domain.service.MainDashboardService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MainDashboardServiceImpl(
    private val analyticsService: AnalyticsService,
) : MainDashboardService {
    override suspend fun getScaling(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
    ): ProjectScaling = coroutineScope {
        val users = async {
            analyticsService.getTotalUsers(project, dateRange)
        }

        val activeUsers = async {
            analyticsService.getActiveUsers(project, dateRange)
        }

        val errors = async {
            analyticsService.getTotalErrors(project, dateRange)
        }

        val events = async {
            analyticsService.getTotalEvents(project, dateRange)
        }

        return@coroutineScope ProjectScaling(
            totalUsers = users.await(),
            activeUsers = activeUsers.await(),
            totalCrashes = errors.await(),
            triggeredEvents = events.await(),
        )
    }
}