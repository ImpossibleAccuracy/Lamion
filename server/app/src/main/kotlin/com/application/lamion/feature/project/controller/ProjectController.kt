package com.application.lamion.feature.project.controller

import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.project.controller.mapper.toDto
import com.application.lamion.feature.project.controller.payload.CreateProjectRequest
import com.application.lamion.feature.project.controller.payload.UpdateProjectRequest
import com.application.lamion.feature.project.domain.ProjectFeatureService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.payload.ProjectDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project")
@SecurityRequirement(name = "jwt")
class ProjectController(
    private val projectFeatureService: ProjectFeatureService,
) : BaseController() {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun create(
        @RequestBody @Valid body: CreateProjectRequest
    ): ProjectDto = endpoint("create project") {
        projectFeatureService
            .create(
                owner = account,
                title = body.title,
                description = body.description
            )
            .toDto()
    }

    @GetMapping
    suspend fun list(): List<ProjectDto> = endpoint("projects list") {
        projectFeatureService
            .list(account)
            .map(ProjectDomain::toDto)
    }

    @PatchMapping("/{pId}")
    suspend fun update(
        @PathVariable("pId") projectId: Id,
        @RequestBody @Valid body: UpdateProjectRequest,
    ) = endpoint("update project") {
        projectFeatureService
            .require(projectId, account)
            .let { projectDomain ->
                projectFeatureService
                    .update(
                        project = projectDomain,
                        account = account,
                        title = body.title,
                        description = body.description,
                    )
                    .toDto()
            }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{pId}")
    suspend fun delete(
        @PathVariable("pId") projectId: Id
    ): Unit = endpoint("delete project") {
        projectFeatureService
            .require(projectId, account)
            .let { projectDomain ->
                projectFeatureService.delete(projectDomain, account)
            }
    }
}