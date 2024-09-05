package com.application.lamion.data.repository

import com.application.lamion.domain.model.EventDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EventRepository : JpaRepository<EventDomain, Int> {
    fun findByTitleIgnoreCaseAndApplicationId(title: String, appId: Int): Optional<EventDomain>

    fun existsByTitle(title: String): Boolean
}
