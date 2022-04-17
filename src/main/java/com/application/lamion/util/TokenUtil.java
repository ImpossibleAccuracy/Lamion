package com.application.lamion.util;

import com.application.lamion.model.User;
import com.application.lamion.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {
    private UserService service;

    @Autowired
    public TokenUtil(UserService service) {
        this.service = service;
    }

    public String create(User user) {
        return JWT.create()
                .withIssuer("auth0")
                .withClaim(User.class.getSimpleName(), user.getEmail())
                .sign(getAlgorithm());
    }

    public User verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm())
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            Claim emailClaim = jwt.getClaim(User.class.getSimpleName());

            String email = emailClaim.as(String.class);
            return service.findByEmail(email);
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256("secret");
    }
}
