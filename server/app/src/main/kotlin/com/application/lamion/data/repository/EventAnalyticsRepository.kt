package com.application.lamion.data.repository

import com.application.lamion.domain.model.EventAnalyticsDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventAnalyticsRepository : JpaRepository<EventAnalyticsDomain, Int> {
    fun findAllByApplicationId(appId: Int): List<EventAnalyticsDomain>
}
