package com.application.lamion.feature.projects.users.data.service

import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.ComparisonDomain
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.users.domain.service.UsersService
import kotlinx.datetime.LocalDate
import org.springframework.stereotype.Service

@Service
class UsersServiceImpl : UsersService {
    override suspend fun getTotalUsers(project: ProjectDomain): ChartDomain<LocalDate, Long> {
        /*
        select count(u.id)
        from projectuser u
        where u.project_id = 1
         */

        TODO("Not yet implemented")
    }

    override suspend fun getActiveUsers(project: ProjectDomain): ChartDomain<LocalDate, Long> {
        // Replace date and count(e.id) with actual value
        /*
        select count(u.id)
        from projectuser u
        inner join "event" e on e.user_id = u.id
        where u.project_id = 1
            and e.created_at >= '2024-08-01'
        having count(e.id) > 100
         */

        TODO("Not yet implemented")
    }

    override suspend fun getGrowthRate(project: ProjectDomain): ComparisonDomain<Double> {
        // Make 2 sql queries for current and prev months
        /*
        select count(e.id)
        from "event" e
        inner join projectfunction f ON e.function_id = f.id
        where f.project_id = 1
            and e.created_at >= '2024-08-01'
            and e.created_at <= '2024-09-01'
         */

        TODO("Not yet implemented")
    }
}