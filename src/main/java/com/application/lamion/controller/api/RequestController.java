package com.application.lamion.controller.api;

import com.application.lamion.exception.BadRequest;
import com.application.lamion.exception.Forbidden;
import com.application.lamion.exception.NotFound;
import com.application.lamion.exception.Unauthorized;
import com.application.lamion.form.RequestForm;
import com.application.lamion.model.EventAnalytics;
import com.application.lamion.model.Request;
import com.application.lamion.model.RequestAnalytics;
import com.application.lamion.model.User;
import com.application.lamion.service.AppService;
import com.application.lamion.service.EventService;
import com.application.lamion.service.RequestService;
import com.application.lamion.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/application/{app_pk}/event/{event_pk}/request")
public class RequestController {
    private AppService appService;
    private EventService eventService;
    private RequestService requestService;

    private TokenUtil tokenUtil;

    @Autowired
    public RequestController(AppService appService, EventService eventService, RequestService requestService, TokenUtil tokenUtil) {
        this.appService = appService;
        this.eventService = eventService;
        this.requestService = requestService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    public ResponseEntity create(@PathVariable("app_pk") long app_pk,
                                 @PathVariable("event_pk") long event_pk,
                                 @RequestBody(required = false) RequestForm form) {
        if (form != null && !form.isValid()) {
            return ResponseEntity.badRequest().body(new BadRequest());
        }

        if (appService.exists(app_pk) && eventService.exists(event_pk)) {
            Request request = new Request();
            if (form != null) {
                request.setDevice(form.device);
            }
            request.setEventId(event_pk);
            request.setDate(new Date());

            requestService.create(request);

            return ResponseEntity.status(202).body(null);
        } else {
            return ResponseEntity.status(404).body(new NotFound());
        }
    }

    @GetMapping
    public ResponseEntity list(@RequestHeader("Authorization") String token,
                               @PathVariable("app_pk") long app_pk,
                               @PathVariable("event_pk") long event_pk) {
        ResponseEntity valid = validateRequest(token, app_pk, event_pk);
        if (valid != null)
            return valid;

        List<Request> requests = requestService.findAllByEventId(event_pk);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/analytics")
    public ResponseEntity analytics(@RequestHeader("Authorization") String token,
                                    @PathVariable("app_pk") long app_pk,
                                    @PathVariable("event_pk") long event_pk) {
        ResponseEntity valid = validateRequest(token, app_pk, event_pk);
        if (valid != null)
            return valid;

        List<RequestAnalytics> requests = requestService.findAnalytics(event_pk);
        return ResponseEntity.ok(requests);
    }

    private ResponseEntity validateRequest(@RequestHeader("Authorization") String token,
                                           @PathVariable("app_pk") long app_pk,
                                           @PathVariable("event_pk") long event_pk) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (appService.hasAccess(app_pk, user.getId())) {
            EventAnalytics event = eventService.find(event_pk);
            if (event == null) {
                return ResponseEntity.status(404).body(new NotFound());
            }

            if (event.getApplicationId() == app_pk) {
                return null;
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
