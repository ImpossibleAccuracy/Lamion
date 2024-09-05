package com.application.lamion.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "request", schema = "public")
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "event_id")
    private int eventId;
    @Column(name = "device")
    private String device;
    @Column(name = "date")
    private Date date;

    public Request() {
    }

    public Request(int id, int eventId, String device, Date date) {
        this.id = id;
        this.eventId = eventId;
        this.device = device;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (id != request.id) return false;
        if (eventId != request.eventId) return false;
        return Objects.equals(device, request.device);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (device != null ? device.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", device='" + device + '\'' +
                '}';
    }
}
