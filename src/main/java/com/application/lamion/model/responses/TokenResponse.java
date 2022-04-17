package com.application.lamion.model.responses;

import com.application.lamion.model.User;

public class TokenResponse {
    public User user;
    public String token;

    public TokenResponse() {
    }

    public TokenResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
