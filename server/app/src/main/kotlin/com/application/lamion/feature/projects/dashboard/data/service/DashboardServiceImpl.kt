package com.application.lamion.feature.projects.dashboard.data.service

import com.application.lamion.data.datasource.EventDataSource
import com.application.lamion.domain.model.ComparisonDomain
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.dashboard.data.datasource.DashboardDataSource
import com.application.lamion.feature.projects.dashboard.domain.model.ProjectScaling
import com.application.lamion.feature.projects.dashboard.domain.service.DashboardService
import com.application.lamion.utils.asyncDbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DashboardServiceImpl : DashboardService {
    companion object {
        const val ACTIVE_USERS_MIN_EVENTS = 5L
    }

    override suspend fun getScaling(
        project: ProjectDomain,
        dateRange: DateRange,
    ): ProjectScaling = coroutineScope {
        val users = async {
            val actual = asyncDbQuery {
                DashboardDataSource.getTotalUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                )
            }

            val past = asyncDbQuery {
                DashboardDataSource.getTotalUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val activeUsers = async {
            val actual = asyncDbQuery {
                DashboardDataSource.getActiveUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                    minEventsToActive = ACTIVE_USERS_MIN_EVENTS,
                )
            }

            val past = asyncDbQuery {
                DashboardDataSource.getActiveUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                    minEventsToActive = ACTIVE_USERS_MIN_EVENTS,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val errors = async {
            val actual = asyncDbQuery {
                DashboardDataSource.getErrorsCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                )
            }

            val past = asyncDbQuery {
                DashboardDataSource.getErrorsCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val events = async {
            val actual = asyncDbQuery {
                EventDataSource.getEventsCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                )
            }

            val past = asyncDbQuery {
                EventDataSource.getEventsCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        return@coroutineScope ProjectScaling(
            totalUsers = users.await(),
            activeUsers = activeUsers.await(),
            totalCrashes = errors.await(),
            triggeredEvents = events.await(),
        )
    }
}