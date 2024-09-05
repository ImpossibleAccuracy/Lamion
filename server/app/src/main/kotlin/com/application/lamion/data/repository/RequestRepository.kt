package com.application.lamion.data.repository

import com.application.lamion.domain.model.RequestDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<RequestDomain, Int> {
    fun findAllByEventIdOrderByIdDesc(eventId: Int): List<RequestDomain>
}
