package com.application.lamion.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "app_analytics", schema = "public")
public class AppAnalytics {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "date")
    private Date date;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "events_count")
    private long eventsCount;
    @Column(name = "requests_count")
    private long requestsCount;

    public AppAnalytics() {
    }

    public AppAnalytics(long id, String title, String description, Date date, long userId, long eventsCount, long requestsCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.userId = userId;
        this.eventsCount = eventsCount;
        this.requestsCount = requestsCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEventsCount() {
        return eventsCount;
    }

    public void setEventsCount(long eventsCount) {
        this.eventsCount = eventsCount;
    }

    public long getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(long requestsCount) {
        this.requestsCount = requestsCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppAnalytics that = (AppAnalytics) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (!title.equals(that.title)) return false;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "AppAnalytics{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", eventsCount=" + eventsCount +
                ", requestsCount=" + requestsCount +
                '}';
    }
}
