package com.application.lamion.controller.api;

import com.application.lamion.exception.BadRequest;
import com.application.lamion.exception.Unauthorized;
import com.application.lamion.model.User;
import com.application.lamion.model.responses.TokenResponse;
import com.application.lamion.service.UserService;
import com.application.lamion.util.Password;
import com.application.lamion.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {
    private static final String USER_NOT_FOUND = "User with such credentials does not exist.";
    private static final String USER_ALREADY_EXIST = "User with such credentials already exist.";

    private UserService userService;
    private TokenUtil tokenUtil;

    @Autowired
    public UserController(UserService userService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @GetMapping("/user")
    public ResponseEntity profile(@RequestHeader("Authorization") String token) {
        if (token == null) {
            return ResponseEntity.status(401).body(new Unauthorized());
        }

        User user = tokenUtil.verify(token);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginForm form) {
        if (form.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new BadRequest());
        }

        User user = userService.findByEmail(form.email);

        if (user != null && Password.verify(form.password, user.getPassword())) {
            String token = tokenUtil.create(user);
            return ResponseEntity.ok(new TokenResponse(user, token));
        } else {
            return ResponseEntity.badRequest().body(
                    new BadRequest(USER_NOT_FOUND));
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody LoginForm data) {
        if (userService.existsByEmail(data.email)) {
            return ResponseEntity.badRequest().body(
                    new BadRequest(USER_ALREADY_EXIST));
        }

        User user = new User();
        user.setEmail(data.email);
        user.setPassword(Password.hash(data.password));
        userService.create(user);

        String token = tokenUtil.create(user);
        return ResponseEntity.ok(new TokenResponse(user, token));
    }

    public static class LoginForm {
        public String email;
        public String password;

        public boolean isEmpty() {
            return ((email == null || email.isBlank()) || (password == null || password.isBlank()));
        }
    }
}
