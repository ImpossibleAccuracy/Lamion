package com.application.lamion.repository;

import com.application.lamion.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByTitleAndApplicationId(String title, long appId);
    boolean existsByTitle(String title);
}
