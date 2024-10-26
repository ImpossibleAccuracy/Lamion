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
import kotlinx.coroutines.async
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/project/{pId}/activity")
class ActivityController(
    private val projectService: ProjectService,
    private val activityService: ActivityFeatureService,
) {
    @GetMapping("/full")
    suspend fun dashboard(
        @PathVariable("pId") projectId: Id,
        @RequestParam("date", required = false) date: LocalDate = LocalDate.now(),
    ): ActivityResponse = secured {
        projectService
            .require(projectId, it.account)
            .let { project ->
                activityService
                    .getActivity(project, date)
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
                val userActivity = async { activityService.getUserActivityTime(date, project) }
                val topFeatures = async { activityService.getTopFeatures(date, project) }

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