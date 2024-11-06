package com.application.lamion.feature.projects.feature.controller

import com.application.lamion.domain.exception.InvalidArgumentsException
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.feature.controller.mapper.toDto
import com.application.lamion.feature.projects.feature.controller.mapper.toPartialDto
import com.application.lamion.feature.projects.feature.domain.model.FunctionDomain
import com.application.lamion.feature.projects.feature.domain.service.FeatureService
import com.application.lamion.feature.projects.feature.domain.service.FunctionService
import com.application.lamion.feature.shared.payload.FunctionDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/functions")
@SecurityRequirement(name = "jwt")
class FunctionController(
    private val projectService: ProjectService,
    private val featureService: FeatureService,
    private val functionService: FunctionService,
) {
    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p") page: Long,
    ): List<FunctionDto.Partial> = secured {
        // TODO: change return type from List to Flow for entire project
        projectService
            .require(projectId, it.account)
            .let { project ->
                functionService.list(project, page)
            }
            .map(FunctionDomain.Partial::toPartialDto)
    }

    @GetMapping("/search")
    suspend fun search(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p") page: Long,
        @RequestParam("q", required = false) globalSearch: String? = null,
        @RequestParam("n", required = false) name: String? = null,
        @RequestParam("f", required = false) features: List<Id>? = null,
        @RequestParam("t", required = false) tags: List<Id>? = null,
    ): List<FunctionDto.Detailed> = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                features?.let {
                    if (featureService.exists(project, features)) {
                        throw InvalidArgumentsException("One or more feature was not found")
                    }
                }

                functionService.search(
                    project = project,
                    globalSearch = globalSearch,
                    name = name,
                    features = features,
                    tags = tags
                )
            }
            .map(FunctionDomain.Detailed::toDto)
    }
}