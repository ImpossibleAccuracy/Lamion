package com.application.lamion.exception;

public class Forbidden {
    public static final String DETAILS = "Forbidden";

    public final String details;

    public Forbidden() {
        details = DETAILS;
    }

    public Forbidden(String details) {
        this.details = details;
    }
}
