package com.application.lamion.feature.projects.activity.controller

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.DateRange
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.activity.controller.payload.ActivityDetailsDto
import com.application.lamion.feature.projects.activity.controller.payload.ActivityResponse
import com.application.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.application.lamion.feature.shared.controller.BaseController
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.utils.atStartOfDay
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate as JavaLocalDate

@RestController
@RequestMapping("/project/{pId}/activity")
@SecurityRequirement(name = "jwt")
class ActivityController(
    private val projectService: ProjectService,
    private val activityService: ActivityFeatureService,
) : BaseController() {
    companion object {
        private const val DEFAULT_ACTIVITY_FEATURES_COUNT = 5
    }

    @GetMapping("/full")
    suspend fun full(
        @PathVariable("pId") projectId: Id,
        @RequestParam("date", required = false) jDate: JavaLocalDate = JavaLocalDate.now(),
    ): ActivityResponse = endpoint("activity full") {
        projectService
            .require(projectId, account)
            .let { project ->
                val startDate = LocalDate(jDate.year, jDate.monthValue, 1)
                val endDate = startDate.plus(DatePeriod(months = 1))

                activityService
                    .getProjectActivity(
                        project = project,
                        dateRange = DateRange(
                            start = startDate.atStartOfDay(),
                            end = endDate.atStartOfDay(),
                        )
                    )
                    .map(CalendarItemDomain::toDto)
            }
            .let(::ActivityResponse)
    }

    @GetMapping("/{date}")
    suspend fun details(
        @PathVariable("pId") projectId: Id,
        @PathVariable("date") jDate: JavaLocalDate,
        @RequestParam("fCount", required = false) featuresCount: Int = DEFAULT_ACTIVITY_FEATURES_COUNT,
    ): ActivityDetailsDto = endpoint("activity details") {
        projectService
            .require(projectId, account)
            .let { project ->
                val date = LocalDate(jDate.year, jDate.monthValue, jDate.dayOfMonth)
                val dateRange = DateRange(
                    start = date.atStartOfDay(),
                    end = date.plus(DatePeriod(days = 1)).atStartOfDay(),
                )

                val detailsDeferred = logTimeAsync("Activity details querying took: %s") {
                    activityService.details(date, project)
                }

                val userActivity = logTimeAsync("User activity time querying took: %s") {
                    activityService.getUserActivityTime(
                        project = project,
                        dateRange = dateRange,
                    )
                }

                val topFeatures = logTimeAsync("Top features querying took: %s") {
                    activityService.getTopFeatures(
                        project = project,
                        dateRange = dateRange,
                        count = featuresCount,
                    )
                }

                val details = detailsDeferred.await()

                ActivityDetailsDto(
                    date = details.date,
                    activeUsers = details.activeUsers,
                    totalEvents = details.totalEvents,
                    crashes = details.crashes,
                    topFeatures = topFeatures.await().map(FeatureWithEvents::toDto),
                    userActivityTime = userActivity.await().toDto(),
                )
            }
    }
}