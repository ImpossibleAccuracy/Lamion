package com.lamion.feature.logger

import com.lamion.domain.DeviceService
import com.lamion.domain.EventService
import com.lamion.domain.ProjectService
import com.lamion.domain.UserService
import com.lamion.feature.logger.payload.LogEventsRequest
import jakarta.validation.Valid
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event")
class EventLoggerController(
    private val projectService: ProjectService,
    private val userService: UserService,
    private val deviceService: DeviceService,
    private val eventService: EventService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/log")
    suspend fun logEvents(
        @RequestHeader("Authorization", required = true) accessKey: String,
        @RequestBody @Valid body: LogEventsRequest
    ) = coroutineScope {
        projectService
            .findProjectByAccessKey(accessKey)
            .let { projectId ->
                val userDeferred = async {
                    userService.findOrCreateUser(
                        projectId = projectId,
                        deviceKey = body.user.deviceKey,
                        clientKey = body.user.clientKey
                    )
                }

                val deviceDeferred = async {
                    deviceService.findOrCreateDevice(
                        name = body.device.name,
                        platform = body.device.platform
                    )
                }

                val user = userDeferred.await()
                val device = deviceDeferred.await()

                eventService.logEvents(
                    projectId = projectId,
                    userId = user,
                    deviceId = device,
                    events = body.events.map {
                        it.function to it.createdAt.toKotlinLocalDateTime()
                    }
                )
            }
    }
}