package com.application.lamion.feature.projects.activity.data

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.ChartDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.projects.activity.domain.model.ActivityDetails
import com.application.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.application.lamion.feature.projects.dashboard.domain.service.DashboardService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class ActivityFeatureServiceImpl : ActivityFeatureService {
    override suspend fun getProjectActivity(project: ProjectDomain, month: LocalDate): List<CalendarItemDomain> {
        // Make 3 queries with unique filtering, counting, etc.

        TODO("Not yet implemented")
    }

    /**
     * @return TODO replace [ActivityDetails] with [CalendarItemDomain]
     */
    override suspend fun details(date: LocalDate, project: ProjectDomain): ActivityDetails {
        // Make 3 queries with unique filtering, counting, etc.
        // and search by single date
        TODO("Not yet implemented")
    }

    /**
     * See [DashboardService.getTopFeatures]
     */
    override suspend fun getTopFeatures(date: LocalDate, project: ProjectDomain): List<FeatureWithEvents> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserActivityTime(date: LocalDate, project: ProjectDomain): ChartDomain<LocalTime, Long> {
        // Query all events by past month
        // And compute it's time of day
        // Group by hour
        TODO("Not yet implemented")
    }
}