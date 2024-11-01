package com.application.lamion.feature.projects.users.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.users.domain.service.DeviceService
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.payload.dto.DeviceDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/devices")
@SecurityRequirement(name = "jwt")
class DevicesController(
    private val projectService: ProjectService,
    private val deviceService: DeviceService,
) {
    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period") period: TimePeriod,
        @RequestParam("p") page: Long,
    ): List<DeviceDto.Detailed> = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                deviceService.getDevices(
                    project = project,
                    period = period,
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