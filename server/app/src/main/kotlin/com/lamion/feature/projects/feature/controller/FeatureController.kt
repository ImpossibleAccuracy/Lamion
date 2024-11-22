package com.lamion.feature.projects.feature.controller

import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.domain.exception.ResourceNotFoundException
import com.lamion.domain.model.Id
import com.lamion.domain.model.TimePeriod
import com.lamion.domain.service.errors.ErrorsService
import com.lamion.domain.service.event.EventService
import com.lamion.domain.service.project.ProjectService
import com.lamion.feature.projects.feature.controller.mapper.toDto
import com.lamion.feature.projects.feature.controller.mapper.toPartialDto
import com.lamion.feature.projects.feature.controller.payload.request.CreateFeatureRequest
import com.lamion.feature.projects.feature.controller.payload.request.DefaultSort
import com.lamion.feature.projects.feature.controller.payload.request.UpdateFeatureRequest
import com.lamion.feature.projects.feature.controller.payload.response.FeatureDetailsResponse
import com.lamion.feature.projects.feature.controller.payload.response.FeaturesResponse
import com.lamion.feature.projects.feature.controller.payload.response.TopFeaturesResponse
import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.projects.feature.domain.service.ExtendedFeatureService
import com.lamion.feature.projects.function.domain.FunctionService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.mapper.buildProgressDto
import com.lamion.feature.shared.mapper.mapToDto
import com.lamion.feature.shared.payload.FeatureDto
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
    private val extendedFeatureService: ExtendedFeatureService,
    private val functionService: FunctionService,
    private val eventService: EventService,
    private val errorService: ErrorsService,
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
                val extendedDateRange = period.toExtendedDateRange()
                val dateRange = extendedDateRange.toDateRange()

                val totalEventsChart = logTimeAsync("Events group by date querying took: %s") {
                    eventService.countEventsGroupByDate(project, dateRange)
                }
                val totalEventsComparison = logTimeAsync("Events comparison querying took: %s") {
                    eventService.getEventsComparison(project, extendedDateRange)
                }

                val totalFeaturesCount = logTimeAsync("Total features count querying took: %s") {
                    extendedFeatureService.count(project)
                }

                FeaturesResponse(
                    events = buildProgressDto(
                        dateRange = dateRange,
                        chart = totalEventsChart.await(),
                        comparison = totalEventsComparison.await(),
                    ),
                    totalFeatures = totalFeaturesCount.await(),
                )
            }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    suspend fun create(
        @PathVariable("pId") pId: Id,
        @RequestBody @Valid body: CreateFeatureRequest,
    ): FeatureDto.Partial = endpoint("create feature") {
        if (body.functions.any { it == null }) {
            throw InvalidArgumentsException("Functions cannot be null")
        }

        projectService
            .require(pId, account)
            .let { project ->
                if (!functionService.exists(project, body.functions.map { it!! })) {
                    throw InvalidArgumentsException("One or more functions was not found")
                }

                logTime("Feature creation took: %s") {
                    extendedFeatureService.create(
                        project = project,
                        account = account,
                        title = body.title,
                        description = body.description,
                        functions = body.functions.map { it!! },
                    )
                }
            }
            .toPartialDto()
    }

    @GetMapping
    suspend fun list(
        @PathVariable("pId") projectId: Id,
        @RequestParam("p", required = false) page: Long = 0,
        @RequestParam("sort", required = false) sort: DefaultSort = DefaultSort.DEFAULT,
    ): List<FeatureDto.Detailed> = endpoint("list feature") {
        projectService
            .require(projectId, account)
            .let { project ->
                extendedFeatureService
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
                    extendedFeatureService.getTopFeaturesWithEventsCount(
                        project = project,
                        dateRange = dateRange,
                        count = count,
                    )
                }

                val totalEvents = logTimeAsync("Total events count querying took: %s") {
                    eventService.countTotalEvents(project, dateRange)
                }

                val averageEvents = logTimeAsync("Average events count querying took: %s") {
                    eventService.countAverageEventsPerDay(project, dateRange)
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

    @GetMapping("/{featureId}")
    suspend fun details(
        @PathVariable("pId") pId: Id,
        @PathVariable("featureId") featureId: Id,
        @RequestParam("period", required = false) period: TimePeriod = TimePeriod.DEFAULT,
    ): FeatureDetailsResponse = endpoint("feature details") {
        projectService
            .require(pId, account)
            .let { projectDomain ->
                extendedFeatureService.get(featureId, projectDomain)
            }
            .let { feature ->
                val extendedDateRange = period.toExtendedDateRange()
                val dateRange = extendedDateRange.toDateRange()

                val tags = logTimeAsync("Feature tags took: %s") {
                    extendedFeatureService.getFeatureTags(feature)
                }

                val eventsComparison = logTimeAsync("Events comparison took: %s") {
                    eventService.getEventsComparison(feature, extendedDateRange)
                }
                val eventsChart = logTimeAsync("Events group by date took: %s") {
                    eventService.countEventsGroupByDate(feature, dateRange)
                }

                val errorsComparison = logTimeAsync("Errors comparison took: %s") {
                    errorService.getTotalErrorsComparison(feature, extendedDateRange)
                }
                val errorsChart = logTimeAsync("Errors group by date took: %s") {
                    errorService.countErrorsGroupByDateByFeature(feature, dateRange)
                }

                FeatureDetailsResponse(
                    feature = feature.toPartialDto(),
                    tags = tags.await(),
                    events = buildProgressDto(
                        dateRange = dateRange,
                        comparison = eventsComparison.await(),
                        chart = eventsChart.await(),
                    ),
                    errors = buildProgressDto(
                        dateRange = dateRange,
                        comparison = errorsComparison.await(),
                        chart = errorsChart.await(),
                    )
                )
            }
    }

    @PutMapping("/{featureId}")
    suspend fun update(
        @PathVariable("pId") pId: Id,
        @PathVariable("featureId") featureId: Id,
        @RequestBody @Valid body: UpdateFeatureRequest,
    ) = endpoint("update feature") {
        projectService
            .require(pId, account)
            .let { projectDomain ->
                extendedFeatureService.get(featureId, projectDomain)
            }
            .let { feature ->
                extendedFeatureService.update(
                    feature = feature,
                    account = account,
                    title = body.title,
                    description = body.description,
                )
            }
            .toPartialDto()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{featureId}/functions/{functionId}")
    suspend fun detachFunction(
        @PathVariable("pId") pId: Id,
        @PathVariable("featureId") featureId: Id,
        @PathVariable("functionId") functionId: Id,
    ): Unit = endpoint("detach function") {
        projectService
            .require(pId, account)
            .let { project ->
                val feature = extendedFeatureService.get(featureId, project)

                if (!functionService.exists(project, listOf(functionId))) {
                    throw ResourceNotFoundException("Function not found")
                }

                functionService.detachFunction(
                    feature = feature,
                    functionId = functionId
                )
            }
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
                extendedFeatureService.get(featureId, projectDomain)
            }
            .let { feature ->
                extendedFeatureService.delete(
                    feature = feature,
                    account = account,
                )
            }
    }
}