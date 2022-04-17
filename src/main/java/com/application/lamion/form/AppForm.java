package com.application.lamion.form;

public class AppForm {
    public String title;
    public String description;

    public boolean isValid() {
        return ((title != null && !title.isBlank()));
    }
}
