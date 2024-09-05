package com.application.lamion.repository

import com.application.lamion.model.AppAnalytics
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppAnalyticsRepository : JpaRepository<AppAnalytics?, Long?> {
    fun findAllByUserId(userId: Long?): List<AppAnalytics?>?

    fun findFirstByTitleAndUserId(title: String?, userId: Long): AppAnalytics?
}
