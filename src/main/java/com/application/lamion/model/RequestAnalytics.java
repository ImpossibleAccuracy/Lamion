package com.application.lamion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "request_analytics", schema = "public")
public class RequestAnalytics {
    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "device")
    private String device;
    @Column(name = "count")
    private long count;
    @Column(name = "event_id")
    private long eventId;

    public RequestAnalytics() {
    }

    public RequestAnalytics(long id, long eventId, String device, long count) {
        this.id = id;
        this.eventId = eventId;
        this.device = device;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
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
