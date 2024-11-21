package com.lamion.feature.projects.users.data

import com.lamion.domain.model.*
import com.lamion.domain.service.users.UsersService
import com.lamion.utils.asyncDbQuery
import com.lamion.utils.dbQuery
import kotlinx.datetime.LocalDate
import org.springframework.stereotype.Service

@Service
class UsersServiceImpl : UsersService {
    override suspend fun getTotalUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
    ): ComparisonDomain<Long> = ComparisonDomain.fromDateRange(dateRange) {
        asyncDbQuery {
            UserDataSource.getUsersCount(
                projectId = project.id,
                start = it.start,
                end = it.end,
            )
        }
    }

    override suspend fun countActiveUsers(project: ProjectDomain, dateRange: DateRange): Long = dbQuery {
        UserDataSource.getActiveUsersCount(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun getActiveUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange,
    ): ComparisonDomain<Long> = ComparisonDomain.fromDateRange(dateRange) {
        asyncDbQuery {
            UserDataSource.getActiveUsersCount(
                projectId = project.id,
                start = it.start,
                end = it.end,
            )
        }
    }

    override suspend fun countTotalUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        UserDataSource.getUserCountGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }

    override suspend fun countActiveUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long> = dbQuery {
        UserDataSource.getActiveUsersGroupByDate(
            projectId = project.id,
            start = dateRange.start,
            end = dateRange.end,
        )
    }
}