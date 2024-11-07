package com.application.lamion.feature.projects.dashboard.controller

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.domain.service.activity.ActivityService
import com.application.lamion.domain.service.project.ProjectService
import com.application.lamion.feature.projects.dashboard.controller.payload.DashboardResponse
import com.application.lamion.feature.projects.dashboard.domain.service.MainDashboardService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.mapper.toDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/dashboard")
@SecurityRequirement(name = "jwt")
class DashboardController(
    private val projectService: ProjectService,
    private val dashboardService: MainDashboardService,
    private val activityService: ActivityService,
) : BaseController() {
    @GetMapping("/full")
    suspend fun dashboard(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
    ): DashboardResponse = endpoint("dashboard") {
        val project = logTime("Project query: %s") {
            projectService.require(projectId, account)
        }

        val extendedDateRange = period.toExtendedDateRange()
        val dateRange = extendedDateRange.toDateRange()

        val scalingDeferred = logTimeAsync("Scaling querying took: %s") {
            dashboardService.getScaling(
                project = project,
                dateRange = extendedDateRange,
            )
        }

        val topFeaturesDeferred = logTimeAsync("Top features querying took: %s") {
            activityService.getTopFeatures(
                project = project,
                dateRange = dateRange,
                count = 10 // TODO
            )
        }

        val activityDeferred = logTimeAsync("Project activity querying took: %s") {
            activityService.getProjectActivity(
                project = project,
                dateRange = dateRange,
            )
        }

        val userActivityTimeDeferred = logTimeAsync("User activity time querying took: %s") {
            activityService.getUserActivityTime(
                project = project,
                dateRange = dateRange,
            )
        }

        val scaling = scalingDeferred.await()
        val topFeatures = topFeaturesDeferred.await()
        val activity = activityDeferred.await()
        val userActivityTime = userActivityTimeDeferred.await()

        logTime("Mapping took: %s") {
            DashboardResponse(
                title = project.title,
                scaling = DashboardResponse.Scaling(
                    totalUsers = scaling.totalUsers.toDto(),
                    activeUsers = scaling.activeUsers.toDto(),
                    totalCrashes = scaling.totalCrashes.toDto(),
                    triggeredEvents = scaling.triggeredEvents.toDto(),
                ),
                topFeatures = topFeatures.map(FeatureWithEvents::toDto),
                calendar = activity.map(CalendarItemDomain::toDto),
                userActivityTime = userActivityTime.toDto()
            )
        }
    }
}
