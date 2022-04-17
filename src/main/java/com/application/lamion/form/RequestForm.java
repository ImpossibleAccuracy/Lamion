package com.application.lamion.form;

public class RequestForm {
    public String device;

    public boolean isValid() {
        if (device == null) return true;

        String d = device.strip();
        return (d.length() > 0 && d.length() <= 50);
    }
}
