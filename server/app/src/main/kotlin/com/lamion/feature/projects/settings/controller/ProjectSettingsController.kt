package com.lamion.feature.projects.settings.controller

import com.lamion.domain.model.Id
import com.lamion.domain.service.project.ProjectService
import com.lamion.feature.projects.settings.controller.payload.ProjectSettingsResponse
import com.lamion.feature.projects.settings.domain.service.ProjectAccessKeyService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.payload.AccessKeysDto
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