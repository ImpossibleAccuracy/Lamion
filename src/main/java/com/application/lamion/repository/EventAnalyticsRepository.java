package com.application.lamion.repository;

import com.application.lamion.model.EventAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAnalyticsRepository extends JpaRepository<EventAnalytics, Long> {
    List<EventAnalytics> findAllByApplicationId(long appId);
}
