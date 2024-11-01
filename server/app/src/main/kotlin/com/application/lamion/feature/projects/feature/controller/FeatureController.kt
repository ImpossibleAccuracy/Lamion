package com.application.lamion.feature.projects.feature.controller

import com.application.lamion.domain.exception.InvalidArgumentsException
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.model.TimePeriod
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.feature.controller.mapper.toDto
import com.application.lamion.feature.projects.feature.controller.mapper.toPartialDto
import com.application.lamion.feature.projects.feature.controller.payload.request.CreateFeatureRequest
import com.application.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.application.lamion.feature.projects.feature.controller.payload.request.UpdateFeatureRequest
import com.application.lamion.feature.projects.feature.controller.payload.response.FeaturesResponse
import com.application.lamion.feature.projects.feature.controller.payload.response.TopFeaturesResponse
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.application.lamion.feature.projects.feature.domain.service.FeatureService
import com.application.lamion.feature.projects.feature.domain.service.FunctionService
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.payload.dto.FeatureDto
import com.application.lamion.feature.shared.security.secured
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import kotlinx.coroutines.async
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/features")
@SecurityRequirement(name = "jwt")
class FeatureController(
    private val projectService: ProjectService,
    private val featureService: FeatureService,
    private val functionService: FunctionService,
) {
    companion object {
        private const val DEFAULT_CHART_SIZE = 10
    }

    @GetMapping("/full")
    suspend fun full(
        @PathVariable("pId") projectId: Id,
    ): FeaturesResponse = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                val chart = async { featureService.getTotalEvents(project) }
                val total = async { featureService.getTotalFeaturesCount(project) }

                FeaturesResponse(
                    totalEvents = chart.await().toDto(),
                    totalFeatures = total.await(),
                )
            }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun create(
        @PathVariable("pId") pId: Id,
        @RequestBody @Valid body: CreateFeatureRequest,
    ): FeatureDto.Partial = secured {
        projectService
            .require(pId, it.account)
            .let { project ->
                if (!functionService.checkExists(body.functions)) {
                    throw InvalidArgumentsException("One or more functions was not found")
                }

                featureService.create(
                    project = project,
                    account = it.account,
                    title = body.title,
                    description = body.description,
                    functions = body.functions,
                )
            }
            .toPartialDto()
    }

    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p") page: Long,
        @RequestParam("sort", required = false) sort: FeaturesSort = FeaturesSort.EVENTS_COUNT,
    ): List<FeatureDto.Detailed> = secured {
        featureService
            .list(
                project = projectService.require(projectId, it.account),
                page = page,
                sort = sort,
            )
            .map(FeatureDomain.Detailed::toDto)
    }

    @GetMapping("/chart")
    suspend fun getTopFeatures(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period") period: TimePeriod,
        @RequestParam("count", required = false) count: Int = DEFAULT_CHART_SIZE,
    ): TopFeaturesResponse = secured {
        featureService
            .getTopFeatures(
                project = projectService.require(projectId, it.account),
                period = period,
                count = count,
            )
            .let { data ->
                TODO()
                /*TopFeaturesResponse(
                    items = data.items.mapToDto { key, value ->
                        key.toPartialDto() to value
                    },
                    totalEvents = data.totalEvents,
                    avgEventsPerDay = data.avgEventsPerDay,
                )*/
            }
    }

    @PostMapping("/{featureId}")
    suspend fun update(
        @PathVariable("pId") pId: Id,
        @PathVariable("featureId") featureId: Id,
        @RequestBody @Valid body: UpdateFeatureRequest,
    ): FeatureDto = secured {
        projectService
            .require(pId, it.account)
            .let { projectDomain ->
                featureService.get(featureId, projectDomain)
            }
            .let { feature ->
                featureService.update(
                    feature = feature,
                    account = it.account,
                    title = body.title,
                    description = body.description,
                )
            }
            .toPartialDto()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{featureId}")
    suspend fun delete(
        @PathVariable("pId") pId: Id,
        @PathVariable("featureId") featureId: Id,
    ): Unit = secured {
        projectService
            .require(pId, it.account)
            .let { projectDomain ->
                featureService.get(featureId, projectDomain)
            }
            .let { feature ->
                featureService.delete(
                    feature = feature,
                    account = it.account,
                )
            }
    }
}