package com.application.lamion.feature.projects.activity.controller

import com.application.lamion.domain.model.CalendarItemDomain
import com.application.lamion.domain.model.FeatureWithEvents
import com.application.lamion.domain.model.Id
import com.application.lamion.domain.service.ProjectService
import com.application.lamion.feature.projects.activity.controller.payload.ActivityDetailsDto
import com.application.lamion.feature.projects.activity.controller.payload.ActivityResponse
import com.application.lamion.feature.projects.activity.domain.service.ActivityFeatureService
import com.application.lamion.feature.shared.mapper.toDto
import com.application.lamion.feature.shared.security.secured
import com.application.lamion.utils.atStartOfDay
import com.application.lamion.utils.atStartOfMonth
import com.application.lamion.utils.now
import com.application.lamion.utils.toDateTime
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.async
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/project/{pId}/activity")
@SecurityRequirement(name = "jwt")
class ActivityController(
    private val projectService: ProjectService,
    private val activityService: ActivityFeatureService,
) {
    @GetMapping("/full")
    suspend fun full(
        @PathVariable("pId") projectId: Id,
        @RequestParam("date", required = false) date: LocalDate = LocalDate.now(),
    ): ActivityResponse = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                activityService
                    .getProjectActivity(
                        project,
                        date.atStartOfMonth().toDateTime(),
                        null,
                    )
                    .map(CalendarItemDomain::toDto)
            }
            .let(::ActivityResponse)
    }

    @GetMapping("/{date}")
    suspend fun details(
        @PathVariable("pId") projectId: Id,
        @PathVariable("date") date: LocalDate,
    ): ActivityDetailsDto = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                val detailsDeferred = async { activityService.details(date, project) }
                val userActivity = async {
                    activityService.getUserActivityTime(
                        project = project,
                        start = date,
                        end = date,
                    )
                }
                val topFeatures = async {
                    activityService.getTopFeatures(
                        project = project,
                        start = date.atStartOfDay(),
                        end = date.plus(DatePeriod(days = 1)).atStartOfDay(),
                        count = 10 // TODO
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