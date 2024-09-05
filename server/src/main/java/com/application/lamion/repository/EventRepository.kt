package com.application.lamion.repository

import com.application.lamion.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event?, Long?> {
    fun findByTitleAndApplicationId(title: String?, appId: Long): Event?
    fun existsByTitle(title: String?): Boolean
}
