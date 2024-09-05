package com.application.lamion.data.repository

import com.application.lamion.domain.model.AppAnalyticsDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppAnalyticsRepository : JpaRepository<AppAnalyticsDomain, Int> {
    fun findAllByUserId(userId: Int): List<AppAnalyticsDomain>
}
