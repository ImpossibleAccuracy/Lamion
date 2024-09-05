package com.application.lamion.service;

import com.application.lamion.model.App;
import com.application.lamion.model.AppAnalytics;
import com.application.lamion.repository.AppAnalyticsRepository;
import com.application.lamion.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {
    private AppRepository repository;
    private AppAnalyticsRepository analyticsRepository;

    @Autowired
    public AppService(AppRepository repository, AppAnalyticsRepository analyticsRepository) {
        this.repository = repository;
        this.analyticsRepository = analyticsRepository;
    }

    // CREATE

    public App create(App data) {
        return repository.save(data);
    }

    // READ

    public List<AppAnalytics> get(long userID) {
        return analyticsRepository.findAllByUserId(userID);
    }

    public long count(long userId) {
        return repository.countByUserId(userId);
    }

    public AppAnalytics find(long id) {
        return analyticsRepository.findById(id).orElse(null);
    }

    public boolean exists(long id) {
        return repository.existsById(id);
    }

    public boolean existsByTitleAndUserId(String title, long userID) {
        return repository.existsByTitleAndUserId(title, userID);
    }

    public boolean hasAccess(long id, long userId) {
        return repository.existsByIdAndUserId(id, userId);
    }

    // UPDATE

    public void update(App data) {
        repository.save(data);
    }

    public App updateTitle(long id, String title, String description) {
        App app = repository.findById(id).orElse(null);
        if (app == null) return null;
        app.setTitle(title);
        app.setDescription(description);
        return repository.save(app);
    }

    // DELETE

    public void delete(long id) {
        repository.deleteById(id);
    }
}
