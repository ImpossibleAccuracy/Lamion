package com.application.lamion.feature.projects.settings.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.settings.controller.payload.ProjectSettingsResponse
import com.application.lamion.feature.projects.settings.domain.service.ProjectTokenService
import com.application.lamion.feature.shared.payload.dto.TokenDto
import com.application.lamion.feature.shared.security.secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/project/{pId}//settings")
class ProjectSettingsController(
    private val projectService: ProjectService,
    private val projectTokenService: ProjectTokenService,
) {
    @GetMapping
    suspend fun get(
        @PathVariable("pId") projectId: Id,
    ): ProjectSettingsResponse = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                val tokens = projectTokenService.getTokens(
                    account = it.account,
                    project = project,
                )

                ProjectSettingsResponse(
                    title = project.title,
                    description = project.description,
                    tokens = tokens.map { token ->
                        TokenDto(
                            title = token.title,
                            createdAt = token.createdAt,
                        )
                    }
                )
            }
    }
}