package com.application.lamion.controller.api;

import com.application.lamion.exception.BadRequest;
import com.application.lamion.exception.Forbidden;
import com.application.lamion.exception.NotFound;
import com.application.lamion.exception.Unauthorized;
import com.application.lamion.form.EventForm;
import com.application.lamion.model.Event;
import com.application.lamion.model.EventAnalytics;
import com.application.lamion.model.User;
import com.application.lamion.service.AppService;
import com.application.lamion.service.EventService;
import com.application.lamion.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/application/{app_pk}/event")
public class EventsController {
    private static final String EVENT_ALREADY_EXISTS = "Event with such title already exist.";

    private AppService appService;
    private EventService eventService;

    private TokenUtil tokenUtil;

    @Autowired
    public EventsController(AppService appService, EventService eventService, TokenUtil tokenUtil) {
        this.appService = appService;
        this.eventService = eventService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    public ResponseEntity create(@RequestHeader("Authorization") String token,
                                 @PathVariable("app_pk") int app_pk,
                                 @RequestBody EventForm form) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (!appService.hasAccess(app_pk, user.getId())) {
            return ResponseEntity.status(403).body(new Forbidden());
        }

        if (form.isValid()) {
            if (eventService.existsByTitle(form.title)) {
                return ResponseEntity.badRequest().body(new BadRequest(EVENT_ALREADY_EXISTS));
            }

            Event event = new Event();
            event.setTitle(form.title);
            event.setDate(new Date());
            event.setApplicationId(app_pk);

            eventService.create(event);
            return ResponseEntity.status(201).body(event);
        } else {
            return ResponseEntity.badRequest().body(new BadRequest());
        }
    }

    @GetMapping
    public ResponseEntity list(@RequestHeader("Authorization") String token,
                               @PathVariable("app_pk") long app_pk) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (appService.hasAccess(app_pk, user.getId())) {
            List<EventAnalytics> events = eventService.findAllByApplicationId(app_pk);
            return ResponseEntity.ok(events);
        } else {
            return ResponseEntity.status(404).body(new NotFound());
        }
    }

    @GetMapping("/{pk}")
    public ResponseEntity retrieve(@RequestHeader("Authorization") String token,
                                   @PathVariable("app_pk") long app_pk,
                                   @PathVariable("pk") long pk) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (appService.hasAccess(app_pk, user.getId())) {
            EventAnalytics event = eventService.find(pk);
            if (event == null) {
                return ResponseEntity.status(404).body(new NotFound());
            }

            if (event.getApplicationId() == app_pk) {
                return ResponseEntity.ok(event);
            } else {
                return ResponseEntity.status(403).body(new Forbidden());
            }
        } else {
            return ResponseEntity.status(404).body(new NotFound());
        }
    }

    @DeleteMapping("/{pk}")
    public ResponseEntity delete(@RequestHeader("Authorization") String token,
                                 @PathVariable("app_pk") long app_pk,
                                 @PathVariable("pk") long pk) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (appService.hasAccess(app_pk, user.getId())) {
            EventAnalytics event = eventService.find(pk);
            if (event == null) {
                return ResponseEntity.status(404).body(new NotFound());
            }

            if (event.getApplicationId() == app_pk) {
                eventService.delete(event.getId());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(403).body(new Forbidden());
            }
        } else {
            return ResponseEntity.status(404).body(new NotFound());
        }
    }

    private User login(String token) {
        return tokenUtil.verify(token);
    }
}
