package com.application.lamion.controller.api

import com.application.lamion.exception.Forbidden
import com.application.lamion.exception.NotFound
import com.application.lamion.exception.Unauthorized
import com.application.lamion.model.User
import com.application.lamion.service.AppService
import com.application.lamion.service.EventService
import com.application.lamion.service.RequestService
import com.application.lamion.util.TokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/api/application/{app_pk}/event/{event_pk}/request")
class RequestController @Autowired constructor(
    private val appService: AppService,
    private val eventService: EventService,
    private val requestService: RequestService,
    private val tokenUtil: TokenUtil
) {
    @GetMapping
    fun list(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Long,
        @PathVariable("event_pk") event_pk: Long
    ): ResponseEntity<*> {
        val valid = validateRequest(token, app_pk, event_pk)
        if (valid != null) return valid

        val requests = requestService.findAllByEventId(event_pk)
        return ResponseEntity.ok(requests)
    }

    @GetMapping("/analytics")
    fun analytics(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Long,
        @PathVariable("event_pk") event_pk: Long
    ): ResponseEntity<*> {
        val valid = validateRequest(token, app_pk, event_pk)
        if (valid != null) return valid

        val requests = requestService.findAnalytics(event_pk)
        return ResponseEntity.ok(requests)
    }

    private fun validateRequest(
        @RequestHeader("Authorization") token: String,
        @PathVariable("app_pk") app_pk: Long,
        @PathVariable("event_pk") event_pk: Long
    ): ResponseEntity<*>? {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (appService.hasAccess(app_pk, user.id.toLong())) {
            val event = eventService.find(event_pk)
                ?: return ResponseEntity.status(404)
                    .body(NotFound())

            return if (event.applicationId.toLong() == app_pk) {
                null
            } else {
                ResponseEntity.status(403)
                    .body(Forbidden())
            }
        } else {
            return ResponseEntity.status(404).body(NotFound())
        }
    }

    private fun login(token: String): User? {
        return tokenUtil.verify(token)
    }
}
