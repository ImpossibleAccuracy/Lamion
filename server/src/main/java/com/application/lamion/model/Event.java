package com.application.lamion.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "event", schema = "public")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private Date date;
    @Column(name = "application_id")
    private int applicationId;

    public Event() {
    }

    public Event(int id, String title, Date date, int applicationId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.applicationId = applicationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        if (applicationId != event.applicationId) return false;
        if (!title.equals(event.title)) return false;
        return date.equals(event.date);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (int) (applicationId ^ (applicationId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", applicationId=" + applicationId +
                '}';
    }
}
