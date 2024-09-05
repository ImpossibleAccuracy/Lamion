package com.application.lamion.form;

public class MakeRequestForm {
    public int appId;
    public String event;
    public String device;

    public boolean isValid() {
        return (appId >= 0 && (event != null && !event.isBlank()));
    }
}
