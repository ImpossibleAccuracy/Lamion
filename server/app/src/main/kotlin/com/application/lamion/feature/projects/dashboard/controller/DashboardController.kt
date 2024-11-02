package com.application.lamion.feature.projects.dashboard.controller

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.dashboard.controller.payload.DashboardResponse
import com.application.lamion.feature.projects.dashboard.domain.service.DashboardService
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.async
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/dashboard")
@SecurityRequirement(name = "jwt")
class DashboardController(
    private val projectService: ProjectService,
    private val dashboardService: DashboardService,
    private val activityService: ActivityService,
) {
    @GetMapping("/full")
    suspend fun dashboard(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
    ): DashboardResponse = secured {
        val project = projectService.require(projectId, it.account)

        val dateRange = period.toDateRange()

        val scalingDeferred = async {
            dashboardService.getScaling(
                project = project,
                dateRange = dateRange,
            )
        }

        val topFeatures = async {
            activityService.getTopFeatures(
                project = project,
                start = dateRange.finishStart,
                end = null
            )
        }

        val activity = async {
            activityService.getProjectActivity(
                project = project,
                month = dateRange.finishStart.date // TODO: check
            )
        }

        val userActivityTime = async {
            activityService.getUserActivityTime(
                project = project,
                start = dateRange.finishStart.date,
                end = null,
            )
        }

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
