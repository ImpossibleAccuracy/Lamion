package com.application.lamion.feature.projects.users.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.domain.service.project.ProjectService
import com.application.lamion.feature.projects.users.domain.service.DeviceService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.payload.DeviceDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/devices")
@SecurityRequirement(name = "jwt")
class DevicesController(
    private val projectService: ProjectService,
    private val deviceService: DeviceService,
) : BaseController() {
    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
        @RequestParam("p", required = false) page: Long = 0,
    ): List<DeviceDto.Detailed> = endpoint("devices list") {
        projectService
            .require(projectId, account)
            .let { project ->
                deviceService.getDetailedDeviceList(
                    project = project,
                    dateRange = period.toExtendedDateRange(),
                    page = page
                )
            }
            .map { item ->
                DeviceDto.Detailed(
                    title = item.title,
                    platform = item.platform,
                    activity = item.activity.toDto(),
                    errors = item.errors.toDto(),
                )
            }
    }
}