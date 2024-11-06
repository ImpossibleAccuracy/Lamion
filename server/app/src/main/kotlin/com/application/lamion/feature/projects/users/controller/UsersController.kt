package com.application.lamion.feature.projects.users.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.users.controller.payload.UsersResponse
import com.application.lamion.feature.projects.users.domain.service.DeviceService
import com.application.lamion.feature.projects.users.domain.service.PlatformService
import com.application.lamion.feature.projects.users.domain.service.UsersDashboardService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.payload.DeviceDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/users")
@SecurityRequirement(name = "jwt")
class UsersController(
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

                val totalUsers = logTimeAsync("Total users querying took: %s") {
                    usersService.countUsersGroupByDate(project, dateRange)
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
                    totalUsersChart = totalUsers.await().toDto(),
                    activeUsersChart = activeUsers.await().toDto(),
                    growthRate = growthRate.await().toDto(),
                    userActivityTime = userActivity.await().toDto(),
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
