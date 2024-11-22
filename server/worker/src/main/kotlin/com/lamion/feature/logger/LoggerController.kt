package com.lamion.feature.logger

import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.domain.model.IncomingError
import com.lamion.domain.model.IncomingEvent
import com.lamion.domain.service.DeviceService
import com.lamion.domain.service.EventService
import com.lamion.domain.service.ProjectService
import com.lamion.domain.service.UserService
import com.lamion.feature.logger.payload.LogRequest
import jakarta.validation.Valid
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.toKotlinLocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event")
class LoggerController(
    private val projectService: ProjectService,
    private val userService: UserService,
    private val deviceService: DeviceService,
    private val eventService: EventService,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/log")
    suspend fun logEvents(
        @RequestHeader("Authorization", required = true) accessKey: String,
        @RequestBody @Valid body: LogRequest
    ) = coroutineScope {
        if (body.events.isNullOrEmpty() && body.errors.isNullOrEmpty()) {
            throw InvalidArgumentsException("No data (events or errors) provided")
        }

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

                val eventsDeferred = body.events
                    ?.takeIf { it.isNotEmpty() }
                    ?.let {
                        async {
                            eventService.logEvents(
                                projectId = projectId,
                                userId = user,
                                deviceId = device,
                                events = body.events.map {
                                    IncomingEvent(
                                        function = it.function,
                                        feature = it.feature,
                                        createdAt = it.createdAt.toKotlinLocalDateTime(),
                                    )
                                }
                            )
                        }
                    }

                val errorsDeferred = body.errors
                    ?.takeIf { it.isNotEmpty() }
                    ?.let {
                        async {
                            eventService.logErrors(
                                projectId = projectId,
                                userId = user,
                                deviceId = device,
                                errors = body.errors.map {
                                    IncomingError(
                                        function = it.function,
                                        createdAt = it.createdAt.toKotlinLocalDateTime(),
                                        text = it.text
                                    )
                                }
                            )
                        }
                    }

                eventsDeferred?.await()
                errorsDeferred?.await()
            }
    }
}