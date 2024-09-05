package com.application.lamion.form;

public class EventForm {
    public String title;

    public boolean isValid() {
        return ((title != null && !title.isBlank()));
    }
}
