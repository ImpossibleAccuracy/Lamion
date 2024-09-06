package com.application.lamion.server.controller

import com.application.lamion.data.service.AppService
import com.application.lamion.data.service.EventService
import com.application.lamion.server.mapper.toDto
import com.application.lamion.server.payload.dto.EventAnalyticsDto
import com.application.lamion.server.payload.dto.EventDto
import com.application.lamion.server.payload.request.CreateEventRequest
import com.application.lamion.server.security.UserHolder
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/application/{app_pk}/event")
class EventsController @Autowired constructor(
    private val appService: AppService,
    private val eventService: EventService,
) {
    @PostMapping
    fun create(
        @PathVariable("app_pk") appId: Int,
        @Valid @RequestBody form: CreateEventRequest
    ): EventDto {
        val user = UserHolder.getUserOrThrow()
        val app = appService.getApp(appId, user)

        return eventService
            .create(app, form.title)
            .toDto()
    }

    @GetMapping
    fun list(
        @PathVariable("app_pk") appId: Int
    ): List<EventAnalyticsDto> {
        val user = UserHolder.getUserOrThrow()
        val app = appService.getApp(appId, user)

        return eventService
            .getAllAppEvents(app)
            .map { it.toDto() }
    }

    @GetMapping("/{pk}")
    fun details(
        @PathVariable("app_pk") appId: Int,
        @PathVariable("pk") eventId: Int
    ): EventAnalyticsDto {
        val user = UserHolder.getUserOrThrow()
        val app = appService.getApp(appId, user)

        return eventService.getEventAnalytics(eventId, app).toDto()
    }

    @DeleteMapping("/{pk}")
    fun delete(
        @PathVariable("app_pk") appId: Int,
        @PathVariable("pk") eventId: Int
    ): ResponseEntity<*> {
        val user = UserHolder.getUserOrThrow()
        val app = appService.getApp(appId, user)
        val event = eventService.getEvent(eventId, app)

        eventService.delete(event)

        return ResponseEntity.noContent().build<Any>()
    }
}
