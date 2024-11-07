package com.lamion.feature.logger

import com.lamion.feature.logger.payload.LogEventsBulkRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event")
class EventLoggerController {
    @PostMapping("/log")
    suspend fun logEvents(
        @RequestBody @Valid body: LogEventsBulkRequest
    ) {
        // TODO
    }
}