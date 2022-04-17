package com.application.lamion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController {
    private static final String TEMPLATE_NAME = "index";

    @RequestMapping("/")
    public String index(Model module) {
        return TEMPLATE_NAME;
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return TEMPLATE_NAME;
    }

    @RequestMapping("/register")
    public String register(Model model) {
        return TEMPLATE_NAME;
    }

    @RequestMapping("/app/create")
    public String appCreate(Model model) {
        return TEMPLATE_NAME;
    }

    @RequestMapping("/apps")
    public String appList(Model model) {
        return TEMPLATE_NAME;
    }

    @RequestMapping("/apps/{app_id}")
    public String appDetails(Model model, @PathVariable("app_id") long appId) {
        return TEMPLATE_NAME;
    }

    @RequestMapping("/apps/{app_id}/events/{event_id}")
    public String eventDetails(Model model,
                             @PathVariable("app_id") long appId,
                             @PathVariable("event_id") long eventId) {
        return TEMPLATE_NAME;
    }
}
