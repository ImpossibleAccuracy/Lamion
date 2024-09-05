package com.application.lamion.exception;

public class NotFound {
    public static final String DETAILS = "Not Found";

    public final String details;

    public NotFound() {
        details = DETAILS;
    }

    public NotFound(String details) {
        this.details = details;
    }
}
