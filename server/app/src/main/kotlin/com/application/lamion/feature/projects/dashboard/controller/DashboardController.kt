package com.application.lamion.feature.projects.dashboard.controller

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.dashboard.controller.payload.DashboardResponse
import com.application.lamion.feature.projects.dashboard.domain.service.DashboardService
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/project/{pId}/dashboard")
@SecurityRequirement(name = "jwt")
class DashboardController(
    private val projectService: ProjectService,
    private val dashboardService: DashboardService,
    private val activityService: ActivityService,
) {
    @GetMapping("/full")
    suspend fun dashboard(@PathVariable("pId") projectId: Id): DashboardResponse = secured {
        val project = projectService.require(projectId, it.account)

        val scalingDeferred = async { dashboardService.getScaling(project) }
        val topFeatures = async { dashboardService.getTopFeatures(project) }
        val activity = async { activityService.getProjectActivity(project, LocalDate.now()) }
        val userActivityTime = async { activityService.getUserActivityTime(LocalDate.now(), project) }

        val scaling = scalingDeferred.await()

        DashboardResponse(
            title = project.title,
            scaling = DashboardResponse.Scaling(
                totalUsers = scaling.totalUsers.toDto(),
                activeUsers = scaling.activeUsers.toDto(),
                totalCrashes = scaling.totalCrashes.toDto(),
                triggeredEvents = scaling.triggeredEvents.toDto(),
            ),
            topFeatures = topFeatures.await().map(FeatureWithEvents::toDto),
            calendar = activity.await().map(CalendarItemDomain::toDto),
            userActivityTime = userActivityTime.await().toDto()
        )
    }
}
