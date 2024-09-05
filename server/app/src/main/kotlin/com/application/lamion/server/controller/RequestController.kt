package com.application.lamion.server.controller

import com.application.lamion.data.service.AppService
import com.application.lamion.data.service.EventService
import com.application.lamion.data.service.RequestService
import com.application.lamion.server.mapper.toDto
import com.application.lamion.server.payload.dto.RequestAnalyticsDto
import com.application.lamion.server.payload.dto.RequestDto
import com.application.lamion.server.security.UserHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/application/{app_pk}/event/{event_pk}/request")
class RequestController @Autowired constructor(
    private val appService: AppService,
    private val eventService: EventService,
    private val requestService: RequestService,
) {
    @GetMapping
    fun list(
        @PathVariable("app_pk") appId: Int,
        @PathVariable("event_pk") eventId: Int
    ): List<RequestDto> {
        val user = UserHolder.getUserOrThrow()
        val app = appService.getApp(appId, user)
        val event = eventService.getEvent(eventId, app)

        return requestService
            .getRequests(event)
            .map { it.toDto() }
    }

    @GetMapping("/analytics")
    fun analytics(
        @PathVariable("app_pk") appId: Int,
        @PathVariable("event_pk") eventId: Int
    ): List<RequestAnalyticsDto> {
        val user = UserHolder.getUserOrThrow()
        val app = appService.getApp(appId, user)
        val event = eventService.getEvent(eventId, app)

        return requestService
            .findAnalytics(event)
            .map { it.toDto() }
    }
}
