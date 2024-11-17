package com.lamion.feature.projects.users.controller

import com.lamion.domain.model.Id
import com.lamion.domain.model.TimePeriod
import com.lamion.domain.service.activity.ActivityService
import com.lamion.domain.service.analytics.AnalyticsService
import com.lamion.domain.service.project.ProjectService
import com.lamion.feature.projects.users.controller.payload.UsersResponse
import com.lamion.feature.projects.users.domain.service.DeviceService
import com.lamion.feature.projects.users.domain.service.PlatformService
import com.lamion.feature.projects.users.domain.service.UsersDashboardService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.mapper.buildProgressDto
import com.lamion.feature.shared.mapper.toDateTimeDto
import com.lamion.feature.shared.mapper.toDto
import com.lamion.feature.shared.payload.DeviceDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/users")
@SecurityRequirement(name = "jwt")
class UsersController(
    private val analyticsService: AnalyticsService,
    private val projectService: ProjectService,
    private val usersService: UsersDashboardService,
    private val deviceService: DeviceService,
    private val activityService: ActivityService,
    private val platformService: PlatformService,
) : BaseController() {
    companion object {
        const val TOP_DEVICES_COUNT = 10
    }

    @GetMapping("/full")
    suspend fun full(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
    ): UsersResponse = endpoint("users full") {
        projectService
            .require(projectId, account)
            .let { project ->
                val extendedDateRange = period.toExtendedDateRange()
                val dateRange = extendedDateRange.toDateRange()

                val totalUsersComparison = logTimeAsync("Total users comparison querying took: %s") {
                    analyticsService.getTotalUsers(project, extendedDateRange)
                }
                val totalUsers = logTimeAsync("Total users querying took: %s") {
                    usersService.countUsersGroupByDate(project, dateRange)
                }

                val activeUsersComparison = logTimeAsync("Active users comparison querying took: %s") {
                    analyticsService.getActiveUsers(project, extendedDateRange)
                }
                val activeUsers = logTimeAsync("Active users querying took: %s") {
                    usersService.countActiveUsersGroupByDate(project, dateRange)
                }

                val growthRate = logTimeAsync("Growth rate querying took: %s") {
                    usersService.computeGrowthRate(project, period)
                }

                val platforms = logTimeAsync("Platforms querying took: %s") {
                    platformService.countEventsGroupByPlatforms(project, dateRange)
                }

                val userActivity = logTimeAsync("User activity querying took: %s") {
                    activityService.getUserActivityTime(
                        project = project,
                        dateRange = dateRange,
                    )
                }

                val topDevices = logTimeAsync("Top devices querying took: %s") {
                    deviceService.getPartialDeviceList(
                        project = project,
                        dateRange = extendedDateRange,
                        count = TOP_DEVICES_COUNT,
                    )
                }

                UsersResponse(
                    totalUsers = buildProgressDto(
                        dateRange = dateRange,
                        chart = totalUsers.await(),
                        comparison = totalUsersComparison.await(),
                    ),
                    activeUsers = buildProgressDto(
                        dateRange = dateRange,
                        chart = activeUsers.await(),
                        comparison = activeUsersComparison.await(),
                    ),
                    growthRate = growthRate.await().toDto(),
                    userActivityTime = userActivity.await().toDateTimeDto(),
                    platforms = platforms.await().toDto(),
                    topDevices = topDevices.await().map { item ->
                        DeviceDto.Partial(
                            title = item.title,
                            activity = item.activity.toDto(),
                            platform = item.platform,
                        )
                    }
                )
            }
    }
}
