package com.lamion.feature.projects.settings.controller

import com.lamion.domain.model.Id
import com.lamion.domain.service.project.ProjectService
import com.lamion.feature.projects.settings.controller.payload.CreateAccessKeyRequest
import com.lamion.feature.projects.settings.domain.service.ProjectAccessKeyService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.payload.AccessKeysWithValueDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import kotlinx.datetime.toJavaLocalDate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/access/key")
@SecurityRequirement(name = "jwt")
class TokenController(
    private val projectService: ProjectService,
    private val projectAccessKeyService: ProjectAccessKeyService,
) : BaseController() {
    @PostMapping
    suspend fun create(
        @PathVariable("pId") projectId: Id,
        @RequestBody @Valid request: CreateAccessKeyRequest,
    ): AccessKeysWithValueDto = endpoint("Create access key") {
        projectService
            .require(projectId, account)
            .let {
                projectAccessKeyService.create(
                    account = account,
                    project = it,
                    title = request.title,
                )
            }
            .let {
                AccessKeysWithValueDto(
                    id = it.id,
                    title = it.title,
                    value = it.value,
                    createdAt = it.createdAt.toJavaLocalDate()
                )
            }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{keyId}")
    suspend fun deleteToken(
        @PathVariable("pId") projectId: Id,
        @PathVariable("keyId") keyId: Id,
    ): Unit = endpoint("Delete access key") {
        projectService
            .require(projectId, account)
            .let {
                projectAccessKeyService.get(
                    account = account,
                    project = it,
                    keyId = keyId
                )
            }
            .let {
                projectAccessKeyService.delete(
                    account = account,
                    target = it,
                )
            }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    suspend fun deleteAll(@PathVariable("pId") projectId: Id): Unit =
        endpoint("Delete project access keys") {
            projectService
                .require(projectId, account)
                .let {
                    projectAccessKeyService.deleteAll(
                        account = account,
                        project = it
                    )
                }
        }
}
