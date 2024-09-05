package com.application.lamion.repository

import com.application.lamion.model.Request
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestRepository : JpaRepository<Request?, Long?> {
    fun findAllByEventIdOrderByIdDesc(eventId: Long): List<Request?>?
}
