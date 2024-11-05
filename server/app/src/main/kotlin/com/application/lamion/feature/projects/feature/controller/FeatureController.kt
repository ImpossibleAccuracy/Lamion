package com.application.lamion.feature.projects.feature.controller

import com.application.lamion.domain.exception.InvalidArgumentsException
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.feature.controller.mapper.toDto
import com.application.lamion.feature.projects.feature.controller.mapper.toPartialDto
import com.application.lamion.feature.projects.feature.controller.payload.request.CreateFeatureRequest
import com.application.lamion.feature.projects.feature.controller.payload.request.FeaturesSort
import com.application.lamion.feature.projects.feature.controller.payload.request.UpdateFeatureRequest
import com.application.lamion.feature.projects.feature.controller.payload.response.FeaturesResponse
import com.application.lamion.feature.projects.feature.controller.payload.response.TopFeaturesResponse
import com.application.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.application.lamion.feature.projects.feature.domain.service.EventService
import com.application.lamion.feature.projects.feature.domain.service.FeatureService
import com.application.lamion.feature.projects.feature.domain.service.FunctionService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.mapper.mapToDto
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.model.TimePeriod
import com.application.lamion.feature.shared.payload.FeatureDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import kotlin.math.roundToLong

@RestController
@RequestMapping("/project/{pId}/features")
@SecurityRequirement(name = "jwt")
class FeatureController(
    private val projectService: ProjectService,
    private val featureService: FeatureService,
    private val functionService: FunctionService,
    private val eventService: EventService,
) : BaseController() {
    companion object {
        private const val DEFAULT_CHART_SIZE = 10
    }

    @GetMapping("/full")
    suspend fun full(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
    ): FeaturesResponse = endpoint("features root") {
        projectService
            .require(projectId, account)
            .let { project ->
                val dateRange = period.toSimpleDateRange()

                val chart = logTimeAsync("Events group by date querying took: %s") {
                    featureService.getEventsGroupByDate(project, dateRange)
                }
                val total = logTimeAsync("Total events count querying took: %s") {
                    featureService.getTotalFeaturesCount(project)
                }

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
    ): FeatureDto.Partial = endpoint("create feature") {
        projectService
            .require(pId, account)
            .let { project ->
                if (!functionService.checkExists(project, body.functions)) {
                    throw InvalidArgumentsException("One or more functions was not found")
                }

                logTime("Feature creation took: %s") {
                    featureService.create(
                        project = project,
                        account = account,
                        title = body.title,
                        description = body.description,
                        functions = body.functions,
                    )
                }
            }
            .toPartialDto()
    }

    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p", required = false) page: Long = 0,
        @RequestParam("sort", required = false) sort: FeaturesSort = FeaturesSort.EVENTS_COUNT,
    ): List<FeatureDto.Detailed> = endpoint("list feature") {
        projectService
            .require(projectId, account)
            .let { project ->
                featureService
                    .list(
                        project = project,
                        page = page,
                        sort = sort,
                    )
                    .map(FeatureDomain.Detailed::toDto)
            }
    }

    @GetMapping("/chart")
    suspend fun getTopFeatures(
        @PathVariable("pId") projectId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
        @RequestParam("count", required = false) count: Int = DEFAULT_CHART_SIZE,
    ): TopFeaturesResponse = endpoint("top features") {
        projectService
            .require(projectId, account)
            .let { project ->
                val dateRange = period.toSimpleDateRange()

                val topFeatures = logTimeAsync("Top features querying took: %s") {
                    featureService.getTopFeatures(
                        project = project,
                        dateRange = dateRange,
                        count = count,
                    )
                }

                val totalEvents = logTimeAsync("Total events count querying took: %s") {
                    eventService.getEventsCount(project, dateRange)
                }

                val averageEvents = logTimeAsync("Average events count querying took: %s") {
                    eventService.getAverageEventsPerDay(project, dateRange)
                }

                TopFeaturesResponse(
                    items = topFeatures.await().mapToDto { f, e ->
                        f.toPartialDto() to e
                    },
                    totalEvents = totalEvents.await(),
                    avgEventsPerDay = averageEvents.await().roundToLong(),
                )
            }
    }

    @PostMapping("/{featureId}")
    suspend fun update(
        @PathVariable("pId") pId: Id,
        @PathVariable("featureId") featureId: Id,
        @RequestBody @Valid body: UpdateFeatureRequest,
    ): FeatureDto = endpoint("update feature") {
        projectService
            .require(pId, account)
            .let { projectDomain ->
                featureService.get(featureId, projectDomain)
            }
            .let { feature ->
                featureService.update(
                    feature = feature,
                    account = account,
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
    ): Unit = endpoint("delete feature") {
        projectService
            .require(pId, account)
            .let { projectDomain ->
                featureService.get(featureId, projectDomain)
            }
            .let { feature ->
                featureService.delete(
                    feature = feature,
                    account = account,
                )
            }
    }
}