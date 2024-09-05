package com.application.lamion.server.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class HtmlController {
    companion object {
        private const val TEMPLATE_NAME = "index"
    }

    @RequestMapping("/")
    fun index(): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/login")
    fun login(): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/register")
    fun register(): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/app/create")
    fun appCreate(): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/apps")
    fun appList(): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/apps/{app_id}")
    fun appDetails(): String {
        return TEMPLATE_NAME
    }

    @RequestMapping("/apps/{app_id}/events/{event_id}")
    fun eventDetails(): String {
        return TEMPLATE_NAME
    }
}
