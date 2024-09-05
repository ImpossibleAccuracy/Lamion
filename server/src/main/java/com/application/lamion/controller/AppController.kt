package com.application.lamion.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AppController {
    @RequestMapping("/")
    fun index(module: Model?): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/login")
    fun login(model: Model?): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/register")
    fun register(model: Model?): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/app/create")
    fun appCreate(model: Model?): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/apps")
    fun appList(model: Model?): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/apps/{app_id}")
    fun appDetails(model: Model?, @PathVariable("app_id") appId: Long): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/apps/{app_id}/events/{event_id}")
    fun eventDetails(
        model: Model?,
        @PathVariable("app_id") appId: Long,
        @PathVariable("event_id") eventId: Long
    ): String {
        return TEMPLATE_NAME
    }

    companion object {
        private const val TEMPLATE_NAME = "index"
    }
}
