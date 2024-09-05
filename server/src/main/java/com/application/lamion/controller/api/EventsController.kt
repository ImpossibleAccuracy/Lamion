package com.application.lamion.controller.api

import com.application.lamion.exception.BadRequest
import com.application.lamion.exception.Forbidden
import com.application.lamion.exception.NotFound
import com.application.lamion.exception.Unauthorized
import com.application.lamion.form.EventForm
import com.application.lamion.model.Event
import com.application.lamion.model.User
import com.application.lamion.service.AppService
import com.application.lamion.service.EventService
import com.application.lamion.util.TokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
@RequestMapping("/api/application/{app_pk}/event")
class EventsController @Autowired constructor(
    private val appService: AppService,
    private val eventService: EventService,
    private val tokenUtil: TokenUtil
) {
    @PostMapping
    fun create(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Int,
        @RequestBody form: EventForm
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (!appService.hasAccess(app_pk.toLong(), user.id.toLong())) {
            return ResponseEntity.status(403).body(Forbidden())
        }

        if (form.isValid) {
            if (eventService.existsByTitle(form.title)) {
                return ResponseEntity.badRequest().body(BadRequest(EVENT_ALREADY_EXISTS))
            }

            val event = Event(
                title = form.title!!,
                date = Date(),
                applicationId = app_pk,
            )

            eventService.create(event)
            return ResponseEntity.status(201).body(event)
        } else {
            return ResponseEntity.badRequest().body(BadRequest())
        }
    }

    @GetMapping
    fun list(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Long
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (appService.hasAccess(app_pk, user.id.toLong())) {
            val events = eventService.findAllByApplicationId(app_pk)
            return ResponseEntity.ok(events)
        } else {
            return ResponseEntity.status(404).body(NotFound())
        }
    }

    @GetMapping("/{pk}")
    fun retrieve(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Long,
        @PathVariable("pk") pk: Long
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (appService.hasAccess(app_pk, user.id.toLong())) {
            val event = eventService.find(pk)
                ?: return ResponseEntity.status(404)
                    .body(NotFound())

            return if (event.applicationId.toLong() == app_pk) {
                ResponseEntity.ok(event)
            } else {
                ResponseEntity.status(403)
                    .body(Forbidden())
            }
        } else {
            return ResponseEntity.status(404).body(NotFound())
        }
    }

    @DeleteMapping("/{pk}")
    fun delete(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Long,
        @PathVariable("pk") pk: Long
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (appService.hasAccess(app_pk, user.id.toLong())) {
            val event = eventService.find(pk)
                ?: return ResponseEntity.status(404)
                    .body(NotFound())

            if (event.applicationId.toLong() == app_pk) {
                eventService.delete(event.id.toLong())
                return ResponseEntity.noContent().build<Any>()
            } else {
                return ResponseEntity.status(403).body(Forbidden())
            }
        } else {
            return ResponseEntity.status(404).body(NotFound())
        }
    }

    private fun login(token: String): User? {
        return tokenUtil.verify(token)
    }

    companion object {
        private const val EVENT_ALREADY_EXISTS = "Event with such title already exist."
    }
}
