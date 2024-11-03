package com.application.lamion.feature.projects.settings.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.settings.controller.payload.ProjectSettingsResponse
import com.application.lamion.feature.projects.settings.domain.service.ProjectAccessKeyService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.payload.dto.AccessKeysDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project/{pId}/settings")
@SecurityRequirement(name = "jwt")
class ProjectSettingsController(
    private val projectService: ProjectService,
    private val projectAccessKeyService: ProjectAccessKeyService,
) : BaseController() {
    @GetMapping
    suspend fun get(
        @PathVariable("pId") projectId: Id,
    ): ProjectSettingsResponse = endpoint("project access keys") {
        projectService
            .require(projectId, account)
            .let { project ->
                val accessKeys = projectAccessKeyService.getAccessKeys(
                    account = account,
                    project = project,
                )

                ProjectSettingsResponse(
                    title = project.title,
                    description = project.description,
                    accessKeys = accessKeys.map { token ->
                        AccessKeysDto(
                            title = token.title,
                            createdAt = token.createdAt,
                        )
                    }
                )
            }
    }
}