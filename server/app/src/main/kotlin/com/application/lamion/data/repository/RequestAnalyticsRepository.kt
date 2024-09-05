package com.application.lamion.data.repository

import com.application.lamion.domain.model.RequestAnalyticsDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestAnalyticsRepository : JpaRepository<RequestAnalyticsDomain, Int> {
    fun findAllByEventIdOrderByCountDesc(eventId: Int): List<RequestAnalyticsDomain>
}
