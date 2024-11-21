package com.lamion.feature.projects.dashboard.data.service

import com.lamion.domain.model.ExtendedDateRange
import com.lamion.domain.model.ProjectDomain
import com.lamion.domain.service.errors.ErrorsService
import com.lamion.domain.service.event.EventService
import com.lamion.domain.service.users.UsersService
import com.lamion.feature.projects.dashboard.domain.model.ProjectScaling
import com.lamion.feature.projects.dashboard.domain.service.MainDashboardService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MainDashboardServiceImpl(
    private val eventService: EventService,
    private val errorsService: ErrorsService,
    private val usersService: UsersService,
) : MainDashboardService {
    override suspend fun getScaling(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
    ): ProjectScaling = coroutineScope {
        val users = async {
            usersService.getTotalUsers(project, dateRange)
        }

        val activeUsers = async {
            usersService.getActiveUsers(project, dateRange)
        }

        val errors = async {
            errorsService.getTotalErrorsComparison(project, dateRange)
        }

        val events = async {
            eventService.getEventsComparison(project, dateRange)
        }

        return@coroutineScope ProjectScaling(
            totalUsers = users.await(),
            activeUsers = activeUsers.await(),
            totalCrashes = errors.await(),
            triggeredEvents = events.await(),
        )
    }
}