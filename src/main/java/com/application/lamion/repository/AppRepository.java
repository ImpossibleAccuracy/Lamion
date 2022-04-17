package com.application.lamion.repository;

import com.application.lamion.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
    boolean existsByTitleAndUserId(String title, long userId);

    boolean existsByIdAndUserId(long id, long userId);

    long countByUserId(long userId);
}
