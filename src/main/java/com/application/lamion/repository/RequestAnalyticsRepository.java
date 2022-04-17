package com.application.lamion.repository;

import com.application.lamion.model.RequestAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestAnalyticsRepository extends JpaRepository<RequestAnalytics, Long> {
    List<RequestAnalytics> findAllByEventIdOrderByCountDesc(long eventId);
}
