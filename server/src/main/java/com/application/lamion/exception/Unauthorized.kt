package com.application.lamion.exception;

public class Unauthorized {
    public static final String DETAILS = "Unauthorized";

    public final String details;

    public Unauthorized() {
        details = DETAILS;
    }

    public Unauthorized(String details) {
        this.details = details;
    }
}
