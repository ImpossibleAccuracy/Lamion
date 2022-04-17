package com.application.lamion.service;

import com.application.lamion.model.Event;
import com.application.lamion.model.EventAnalytics;
import com.application.lamion.repository.EventAnalyticsRepository;
import com.application.lamion.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private EventRepository repository;
    private EventAnalyticsRepository analyticsRepository;

    @Autowired
    public EventService(EventRepository repository, EventAnalyticsRepository analyticsRepository) {
        this.repository = repository;
        this.analyticsRepository = analyticsRepository;
    }

    public Event create(Event data) {
        return repository.save(data);
    }


    public List<EventAnalytics> findAllByApplicationId(long appId) {
        return analyticsRepository.findAllByApplicationId(appId);
    }

    public EventAnalytics find(long id) {
        return analyticsRepository.findById(id).orElse(null);
    }

    public Event findByTitle(String title) {
        return repository.findByTitle(title);
    }

    public boolean exists(long id) {
        return repository.existsById(id);
    }

    public boolean existsByTitle(String title) {
        return repository.existsByTitle(title);
    }


    public void update(Event data) {
        repository.save(data);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
