package com.application.lamion.exception;

public class BadRequest {
    public static final String DETAILS = "Bad request";

    public final String details;

    public BadRequest() {
        details = DETAILS;
    }

    public BadRequest(String details) {
        this.details = details;
    }
}
