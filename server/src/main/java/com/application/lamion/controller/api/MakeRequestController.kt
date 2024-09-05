package com.application.lamion.controller.api

import com.application.lamion.exception.BadRequest
import com.application.lamion.exception.NotFound
import com.application.lamion.form.MakeRequestForm
import com.application.lamion.model.Event
import com.application.lamion.model.Request
import com.application.lamion.service.AppService
import com.application.lamion.service.EventService
import com.application.lamion.service.RequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
@RequestMapping("/api")
class MakeRequestController @Autowired constructor(
    private val appService: AppService,
    private val eventService: EventService,
    private val requestService: RequestService
) {
    @PostMapping("request")
    fun makeRequest(@RequestBody form: MakeRequestForm): ResponseEntity<*> {
        if (form.isValid) {
            if (appService.exists(form.appId.toLong())) {
                val event = eventService.findByTitleAndApplicationId(form.event, form.appId.toLong())
                    ?: Event(
                        applicationId = form.appId,
                        title = form.event!!,
                        date = Date(),
                    ).let {
                        eventService.create(it)
                    }

                val request = Request(
                    eventId = event.id,
                    device = form.device!!,
                    date = Date()
                )

                requestService.save(request)

                return ResponseEntity.status(204).build<Any>()
            }

            return ResponseEntity.status(404).body(NotFound())
        }

        return ResponseEntity.badRequest().body(BadRequest())
    }
}
