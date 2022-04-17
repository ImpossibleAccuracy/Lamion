package com.application.lamion.repository;

import com.application.lamion.model.App;
import com.application.lamion.model.AppAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppAnalyticsRepository extends JpaRepository<AppAnalytics, Long> {
    List<AppAnalytics> findAllByUserId(Long userId);

    AppAnalytics findFirstByTitleAndUserId(String title, long userId);
}
