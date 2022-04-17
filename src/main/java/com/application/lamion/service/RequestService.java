package com.application.lamion.service;

import com.application.lamion.model.Request;
import com.application.lamion.model.RequestAnalytics;
import com.application.lamion.repository.RequestAnalyticsRepository;
import com.application.lamion.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private RequestRepository repository;
    private RequestAnalyticsRepository analyticsRepository;

    @Autowired
    public RequestService(RequestRepository repository, RequestAnalyticsRepository analyticsRepository) {
        this.repository = repository;
        this.analyticsRepository = analyticsRepository;
    }

    public Request create(Request request) {
        return repository.save(request);
    }

    public List<Request> findAllByEventId(long eventId) {
        return repository.findAllByEventIdOrderByIdDesc(eventId);
    }

    public List<RequestAnalytics> findAnalytics(long eventId) {
        return analyticsRepository.findAllByEventIdOrderByCountDesc(eventId);
    }
}
