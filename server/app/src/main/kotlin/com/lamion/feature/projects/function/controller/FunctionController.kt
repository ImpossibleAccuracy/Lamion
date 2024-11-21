package com.lamion.feature.projects.function.controller

import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.domain.model.Id
import com.lamion.domain.service.feature.FeatureService
import com.lamion.domain.service.project.ProjectService
import com.lamion.feature.projects.function.domain.FunctionDomain
import com.lamion.feature.projects.function.domain.FunctionService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.payload.FunctionDto
import com.lamion.feature.shared.payload.toDto
import com.lamion.feature.shared.payload.toPartialDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/functions")
@SecurityRequirement(name = "jwt")
class FunctionController(
    private val projectService: ProjectService,
    private val featureService: FeatureService,
    private val functionService: FunctionService,
) : BaseController() {
    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p") page: Long = 0,
    ): List<FunctionDto.Partial> = endpoint("functions list") {
        // TODO: change return type from List to Flow for entire project
        projectService
            .require(projectId, account)
            .let { project ->
                functionService.list(project, page)
            }
            .map(FunctionDomain.Partial::toPartialDto)
    }

    @GetMapping("/feature/{featureId}")
    suspend fun byFeature(
        @PathVariable("pId") projectId: Id,
        @PathVariable("featureId") featureId: Id,
        @RequestParam("p") page: Long,
        @RequestParam("q", required = false) query: String? = null,
    ): List<FunctionDto.WithEvents> = endpoint("partial functions list") {
        projectService
            .require(projectId, account)
            .let {
                featureService.get(featureId, it)
            }
            .let {
                functionService.listWithEventsCount(
                    feature = it,
                    query = query,
                    page = page
                )
            }
            .map {
                FunctionDto.WithEvents(
                    id = it.key.id,
                    title = it.key.title,
                    events = it.value,
                )
            }
    }

    @GetMapping("/search")
    suspend fun search(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p") page: Long,
        @RequestParam("q", required = false) globalSearch: String? = null,
        @RequestParam("n", required = false) name: String? = null,
        @RequestParam("f", required = false) features: List<Id>? = null,
        @RequestParam("t", required = false) tags: List<Id>? = null,
    ): List<FunctionDto.Detailed> = endpoint("functions search") {
        projectService
            .require(projectId, account)
            .let { project ->
                features?.let {
                    if (!featureService.exists(project, features)) {
                        throw InvalidArgumentsException("One or more feature was not found")
                    }
                }

                functionService.search(
                    project = project,
                    globalSearch = globalSearch,
                    name = name,
                    features = features,
                    tags = tags,
                    page = page,
                )
            }
            .map(FunctionDomain.Detailed::toDto)
    }
}