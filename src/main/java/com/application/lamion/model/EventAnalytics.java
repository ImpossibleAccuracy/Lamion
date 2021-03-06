package com.application.lamion.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "event_analytics", schema = "public")
public class EventAnalytics {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private Date date;
    @Column(name = "application_id")
    private long applicationId;
    @Column(name = "requests_count")
    private long requestsCount;
    @Column(name = "most_used_device")
    private String mostUsedDevice;
    @Column(name = "high_demand_time")
    private String highDemandTime;

    public EventAnalytics() {
    }

    public EventAnalytics(long id, String title, Date date, long applicationId, long requestsCount, String mostUsedDevice, String highDemandTime) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.applicationId = applicationId;
        this.requestsCount = requestsCount;
        this.mostUsedDevice = mostUsedDevice;
        this.highDemandTime = highDemandTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(long requestsCount) {
        this.requestsCount = requestsCount;
    }

    public String getMostUsedDevice() {
        return mostUsedDevice;
    }

    public void setMostUsedDevice(String mostUsedDevice) {
        this.mostUsedDevice = mostUsedDevice;
    }

    public String getHighDemandTime() {
        return highDemandTime;
    }

    public void setHighDemandTime(String highDemandTime) {
        this.highDemandTime = highDemandTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventAnalytics that = (EventAnalytics) o;

        if (id != that.id) return false;
        if (applicationId != that.applicationId) return false;
        if (!title.equals(that.title)) return false;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) (applicationId ^ (applicationId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "EventAnalytics{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", applicationId=" + applicationId +
                '}';
    }
}
