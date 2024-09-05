package com.application.lamion.repository

import com.application.lamion.model.RequestAnalytics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestAnalyticsRepository : JpaRepository<RequestAnalytics?, Long?> {
    fun findAllByEventIdOrderByCountDesc(eventId: Long): List<RequestAnalytics?>?
}
