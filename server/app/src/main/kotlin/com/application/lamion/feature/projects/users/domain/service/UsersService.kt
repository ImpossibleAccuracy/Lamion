package com.application.lamion.feature.projects.users.domain.service

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.ComparisonDomain
import com.application.lamion.domain.model.ProjectDomain
import kotlinx.datetime.LocalDate

interface UsersService {
    suspend fun getTotalUsers(project: ProjectDomain): ChartDomain<LocalDate, Long>

    suspend fun getActiveUsers(project: ProjectDomain): ChartDomain<LocalDate, Long>

    suspend fun getGrowthRate(project: ProjectDomain): ComparisonDomain<Double>
}