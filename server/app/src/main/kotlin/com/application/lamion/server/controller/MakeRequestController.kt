package com.application.lamion.server.controller

import com.application.lamion.data.service.AppService
import com.application.lamion.data.service.EventService
import com.application.lamion.data.service.RequestService
import com.application.lamion.server.payload.request.RequestCreationRequest
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class MakeRequestController @Autowired constructor(
    private val appService: AppService,
    private val eventService: EventService,
    private val requestService: RequestService
) {
    @PostMapping("request")
    fun makeRequest(@Valid @RequestBody form: RequestCreationRequest): ResponseEntity<*> {
        val app = appService.getAppUnsecured(form.appId)

        val event = eventService.getEventByTitleOrCreateNew(
            app = app,
            title = form.event
        )

        requestService.create(
            event = event,
            device = form.device,
        )

        return ResponseEntity.status(204).build<Any>()
    }
}
