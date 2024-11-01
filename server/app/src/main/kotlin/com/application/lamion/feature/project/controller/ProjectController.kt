package com.application.lamion.feature.project.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.project.controller.mapper.toDto
import com.application.lamion.feature.project.controller.payload.CreateProjectRequest
import com.application.lamion.feature.project.domain.ProjectFeatureService
import com.application.lamion.feature.shared.payload.dto.ProjectDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project")
@SecurityRequirement(name = "jwt")
class ProjectController(
    private val projectFeatureService: ProjectFeatureService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun create(@RequestBody @Valid body: CreateProjectRequest): ProjectDto = secured {
        projectFeatureService
            .create(
                owner = it.account,
                title = body.title,
                description = body.description
            )
            .toDto()
    }

    @GetMapping
    suspend fun list(): List<ProjectDto> = secured {
        projectFeatureService
            .list(it.account)
            .map(ProjectDomain::toDto)
    }

    // TODO: add update method

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/{pId}")
    suspend fun delete(
        @PathVariable("pId") projectId: Id
    ): Unit = secured {
        projectFeatureService
            .require(projectId, it.account)
            .let { projectDomain ->
                projectFeatureService.delete(projectDomain, it.account)
            }
    }
}