package com.application.lamion.controller.api

import com.application.lamion.exception.BadRequest
import com.application.lamion.exception.Forbidden
import com.application.lamion.exception.NotFound
import com.application.lamion.exception.Unauthorized
import com.application.lamion.form.AppForm
import com.application.lamion.model.App
import com.application.lamion.model.User
import com.application.lamion.model.responses.AppCountResponse
import com.application.lamion.service.AppService
import com.application.lamion.util.TokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin
@RestController
@RequestMapping("/api/application")
class ApplicationController @Autowired constructor(private val service: AppService, private val tokenUtil: TokenUtil) {
    @PostMapping
    fun create(
        @RequestHeader("Authorization") token: String,
        @RequestBody form: AppForm
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (form.isValid) {
            if (service.existsByTitleAndUserId(form.title, user.id.toLong())) {
                return ResponseEntity.badRequest().body(
                    BadRequest(
                        APPLICATION_ALREADY_EXISTS
                    )
                )
            }

            val app = App(
                title = form.title!!,
                description = form.description,
                date = Date(),
                userId = user.id,
            )

            service.create(app)
            return ResponseEntity.ok(app)
        } else {
            return ResponseEntity.badRequest().body(BadRequest())
        }
    }

    @GetMapping
    fun list(@RequestHeader("Authorization") token: String): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        val apps = service.get(user.id.toLong())
        return ResponseEntity.ok(apps)
    }

    @GetMapping("/count")
    fun count(@RequestHeader("Authorization") token: String): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        return ResponseEntity.ok(
            AppCountResponse(
                service.count(user.id.toLong())
            )
        )
    }

    @GetMapping("/{pk}")
    fun retrieve(
        @RequestHeader("Authorization") token: String,
        @PathVariable(value = "pk") pk: Long
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (service.hasAccess(pk, user.id.toLong())) {
            val app = service.find(pk)
            return ResponseEntity.ok(app)
        } else {
            return ResponseEntity.status(404).body(NotFound())
        }
    }

    @PutMapping("/{pk}")
    fun update(
        @RequestHeader("Authorization") token: String,
        @PathVariable(value = "pk") pk: Long,
        @RequestBody form: AppForm
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (!form.isValid) {
            return ResponseEntity.badRequest().body(BadRequest())
        }

        if (!service.exists(pk)) {
            return ResponseEntity.status(404).body(NotFound())
        }

        if (service.hasAccess(pk, user.id.toLong())) {
            val app = service.updateTitle(pk, form.title!!, form.description)
            return ResponseEntity.ok(app)
        } else {
            return ResponseEntity.status(403).body(Forbidden())
        }
    }

    @DeleteMapping("/{pk}")
    fun delete(
        @RequestHeader("Authorization") token: String,
        @PathVariable pk: Long
    ): ResponseEntity<*> {
        val user = login(token)
            ?: return ResponseEntity.status(401)
                .body(Unauthorized())

        if (service.hasAccess(pk, user.id.toLong())) {
            println(pk)
            service.delete(pk)
            return ResponseEntity.noContent().build<Any>()
        } else {
            return ResponseEntity.status(404).body(NotFound())
        }
    }

    private fun login(token: String): User? {
        return tokenUtil.verify(token)
    }

    companion object {
        private const val APPLICATION_ALREADY_EXISTS = "Application with such title already exist."
    }
}
