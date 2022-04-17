package com.application.lamion.controller.api;

import com.application.lamion.exception.BadRequest;
import com.application.lamion.exception.Forbidden;
import com.application.lamion.exception.NotFound;
import com.application.lamion.exception.Unauthorized;
import com.application.lamion.form.AppForm;
import com.application.lamion.model.App;
import com.application.lamion.model.AppAnalytics;
import com.application.lamion.model.User;
import com.application.lamion.model.responses.AppCountResponse;
import com.application.lamion.service.AppService;
import com.application.lamion.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    private static final String APPLICATION_ALREADY_EXISTS = "Application with such title already exist.";

    private AppService service;
    private TokenUtil tokenUtil;

    @Autowired
    public ApplicationController(AppService service, TokenUtil tokenUtil) {
        this.service = service;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    public ResponseEntity create(@RequestHeader("Authorization") String token,
                                 @RequestBody AppForm form) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (form.isValid()) {
            if (service.existsByTitleAndUserId(form.title, user.getId())) {
                return ResponseEntity.badRequest().body(new BadRequest(APPLICATION_ALREADY_EXISTS));
            }

            App app = new App();
            app.setTitle(form.title);
            app.setDescription(form.description);
            app.setDate(new Date());
            app.setUserId(user.getId());

            service.create(app);
            return ResponseEntity.ok(app);
        } else {
            return ResponseEntity.badRequest().body(new BadRequest());
        }
    }

    @GetMapping
    public ResponseEntity list(@RequestHeader("Authorization") String token) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        List<AppAnalytics> apps = service.get(user.getId());
        return ResponseEntity.ok(apps);
    }

    @GetMapping("/count")
    public ResponseEntity count(@RequestHeader("Authorization") String token) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        return ResponseEntity.ok(
            new AppCountResponse(
                service.count(user.getId())));
    }

    @GetMapping("/{pk}")
    public ResponseEntity retrieve(@RequestHeader("Authorization") String token,
                                   @PathVariable(value = "pk") long pk) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (service.hasAccess(pk, user.getId())) {
            AppAnalytics app = service.find(pk);
            return ResponseEntity.ok(app);
        } else {
            return ResponseEntity.status(404).body(new NotFound());
        }
    }

    @PutMapping("/{pk}")
    public ResponseEntity update(@RequestHeader("Authorization") String token,
                                 @PathVariable(value = "pk") long pk,
                                 @RequestBody AppForm form) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (!form.isValid()) {
            return ResponseEntity.badRequest().body(new BadRequest());
        }

        if (!service.exists(pk)) {
            return ResponseEntity.status(404).body(new NotFound());
        }

        if (service.hasAccess(pk, user.getId())) {
            App app = service.updateTitle(pk, form.title, form.description);
            return ResponseEntity.ok(app);
        } else {
            return ResponseEntity.status(403).body(new Forbidden());
        }
    }

    @DeleteMapping("/{pk}")
    public ResponseEntity delete(@RequestHeader("Authorization") String token,
                                 @PathVariable long pk) {
        User user = login(token);
        if (user == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        if (service.hasAccess(pk, user.getId())) {
            System.out.println(pk);
            service.delete(pk);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body(new NotFound());
        }
    }

    private User login(String token) {
        return tokenUtil.verify(token);
    }
}
