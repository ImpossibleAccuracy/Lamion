package com.application.lamion.feature.projects.users.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ActivityService
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.users.controller.payload.UsersResponse
import com.application.lamion.feature.projects.users.domain.service.DeviceService
import com.application.lamion.feature.projects.users.domain.service.PlatformService
import com.application.lamion.feature.projects.users.domain.service.UsersService
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.payload.dto.DeviceDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.async
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/project/{pId}/users")
@SecurityRequirement(name = "jwt")
class UsersController(
    private val projectService: ProjectService,
    private val usersService: UsersService,
    private val deviceService: DeviceService,
    private val activityService: ActivityService,
    private val platformService: PlatformService,
) {
    @GetMapping("/full")
    suspend fun full(
        @PathVariable("pId") projectId: Id,
    ): UsersResponse = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                val totalUsers = async { usersService.getTotalUsers(project) }
                val activeUsers = async { usersService.getActiveUsers(project) }
                val growthRate = async { usersService.getGrowthRate(project) }
                val userActivity = async { activityService.getUserActivityTime(LocalDate.now(), project) }
                val platforms = async { platformService.getPlatforms(project) }
                val topDevices = async { deviceService.getTopDevices(project) }

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
