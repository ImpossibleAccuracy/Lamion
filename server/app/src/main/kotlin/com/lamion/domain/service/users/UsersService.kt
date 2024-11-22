package com.lamion.domain.service.users

import com.lamion.domain.model.*
import kotlinx.datetime.LocalDate

interface UsersService {
    suspend fun getTotalUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun countActiveUsers(
        project: ProjectDomain,
        dateRange: DateRange
    ): Long

    suspend fun getActiveUsers(
        project: ProjectDomain,
        dateRange: ExtendedDateRange
    ): ComparisonDomain<Long>

    suspend fun countTotalUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long>

    suspend fun countActiveUsersGroupByDate(
        project: ProjectDomain,
        dateRange: DateRange
    ): ChartDomain<LocalDate, Long>
}