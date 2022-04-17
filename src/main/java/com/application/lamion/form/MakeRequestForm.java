package com.application.lamion.form;

public class MakeRequestForm {
    public long appId;
    public String event;
    public String device;

    public boolean isValid() {
        return (appId >= 0 && (event != null && !event.isBlank()));
    }
}
