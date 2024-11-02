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
    ): ProjectScaling {
        val users = asyncDbQuery {
            val actual = async {
                DashboardDataSource.getTotalUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                )
            }

            val past = async {
                DashboardDataSource.getTotalUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val activeUsers = asyncDbQuery {
            val actual = async {
                DashboardDataSource.getActiveUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                    minEventsToActive = ACTIVE_USERS_MIN_EVENTS,
                )
            }

            val past = async {
                DashboardDataSource.getActiveUsersCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                    minEventsToActive = ACTIVE_USERS_MIN_EVENTS,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val errors = asyncDbQuery {
            val actual = async {
                DashboardDataSource.getCrashesCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                )
            }

            val past = async {
                DashboardDataSource.getCrashesCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val events = asyncDbQuery {
            val actual = async {
                EventDataSource.getEventsCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.firstStart,
                    end = dateRange.firstEnd,
                )
            }

            val past = async {
                EventDataSource.getEventsCountByCreatedBetween(
                    projectId = project.id,
                    start = dateRange.finishStart,
                    end = dateRange.finishEnd,
                )
            }

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        return ProjectScaling(
            totalUsers = users.await(),
            activeUsers = activeUsers.await(),
            totalCrashes = errors.await(),
            triggeredEvents = events.await(),
        )
    }
}