package com.application.lamion.repository

import com.application.lamion.model.EventAnalytics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventAnalyticsRepository : JpaRepository<EventAnalytics?, Long?> {
    fun findAllByApplicationId(appId: Long): List<EventAnalytics?>?
}
