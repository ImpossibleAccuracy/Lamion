package com.application.lamion.server.controller

import com.application.lamion.data.service.AppService
import com.application.lamion.server.mapper.toDto
import com.application.lamion.server.payload.dto.AppAnalyticsDto
import com.application.lamion.server.payload.dto.AppDto
import com.application.lamion.server.payload.request.CreateAppRequest
import com.application.lamion.server.payload.response.AppCountResponse
import com.application.lamion.server.security.UserHolder
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/application")
class ApplicationController @Autowired constructor(
    private val service: AppService
) {
    @PostMapping("/")
    fun create(@Valid @RequestBody form: CreateAppRequest): AppDto =
        service
            .create(
                title = form.title,
                description = form.description,
                user = UserHolder.getUserOrThrow(),
            )
            .toDto()

    @GetMapping
    fun list(): List<AppAnalyticsDto> =
        service
            .getUserAppsAnalytics(
                user = UserHolder.getUserOrThrow()
            )
            .map { it.toDto() }

    @GetMapping("/count")
    fun count(): AppCountResponse =
        AppCountResponse(
            service.countUserApps(UserHolder.getUserOrThrow())
        )

    @GetMapping("/{pk}")
    fun details(@PathVariable(value = "pk") appId: Int): AppAnalyticsDto =
        service.getAppAnalytics(appId, UserHolder.getUserOrThrow()).toDto()

    @PutMapping("/{pk}")
    fun update(
        @PathVariable(value = "pk") appId: Int,
        @Valid @RequestBody form: CreateAppRequest
    ): AppDto {
        val user = UserHolder.getUserOrThrow()
        val app = service.getApp(appId, user)

        return service
            .update(
                app = app,
                title = form.title,
                description = form.description,
            )
            .toDto()
    }

    @DeleteMapping("/{pk}")
    fun delete(@PathVariable appId: Int): ResponseEntity<*> {
        val user = UserHolder.getUserOrThrow()
        val app = service.getApp(appId, user)

        service.delete(app)

        return ResponseEntity.noContent().build<Any>()
    }
}
