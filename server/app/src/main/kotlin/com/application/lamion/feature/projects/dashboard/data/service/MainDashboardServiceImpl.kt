package com.application.lamion.feature.projects.dashboard.data.service

import com.application.lamion.data.datasource.ErrorDataSource
import com.application.lamion.data.datasource.EventDataSource
import com.application.lamion.data.datasource.UserDataSource
import com.application.lamion.domain.model.ComparisonDomain
import com.application.lamion.domain.model.ExtendedDateRange
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.dashboard.domain.model.ProjectScaling
import com.application.lamion.feature.projects.dashboard.domain.service.MainDashboardService
import com.application.lamion.utils.asyncDbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MainDashboardServiceImpl : MainDashboardService {

    override suspend fun getScaling(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
    ): ProjectScaling = coroutineScope {
        val users = async {
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

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val activeUsers = async {
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

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val errors = async {
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

            ComparisonDomain(
                actual = actual.await(),
                past = past.await(),
            )
        }

        val events = async {
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