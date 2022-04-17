package com.application.lamion.controller.api;

import com.application.lamion.exception.BadRequest;
import com.application.lamion.exception.NotFound;
import com.application.lamion.form.MakeRequestForm;
import com.application.lamion.model.Event;
import com.application.lamion.model.Request;
import com.application.lamion.service.AppService;
import com.application.lamion.service.EventService;
import com.application.lamion.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MakeRequestController {
    private AppService appService;
    private EventService eventService;
    private RequestService requestService;

    @Autowired
    public MakeRequestController(AppService appService, EventService eventService, RequestService requestService) {
        this.appService = appService;
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @PostMapping("request")
    public ResponseEntity makeRequest(@RequestBody MakeRequestForm form) {
        if (form.isValid()) {
            if (appService.exists(form.appId)) {
                Event event = eventService.findByTitle(form.event);
                if (event == null) {
                    event = new Event();
                    event.setApplicationId(form.appId);
                    event.setTitle(form.event);
                    event = eventService.create(event);
                }

                Request request = new Request();
                request.setEventId(event.getId());
                request.setDevice(form.device);
                requestService.create(request);

                return ResponseEntity.status(204).build();
            }

            return ResponseEntity.status(404).body(new NotFound());
        }

        return ResponseEntity.badRequest().body(new BadRequest());
    }
}
