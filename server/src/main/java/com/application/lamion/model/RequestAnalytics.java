package com.application.lamion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "request_analytics", schema = "public")
public class RequestAnalytics {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "device")
    private String device;
    @Column(name = "count")
    private int count;
    @Column(name = "event_id")
    private int eventId;

    public RequestAnalytics() {
    }

    public RequestAnalytics(int id, int eventId, String device, int count) {
        this.id = id;
        this.eventId = eventId;
        this.device = device;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestAnalytics that = (RequestAnalytics) o;

        if (count != that.count) return false;
        return Objects.equals(device, that.device);
    }

    @Override
    public int hashCode() {
        int result = device != null ? device.hashCode() : 0;
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "RequestAnalytics{" +
                "device='" + device + '\'' +
                ", count=" + count +
                '}';
    }
}
