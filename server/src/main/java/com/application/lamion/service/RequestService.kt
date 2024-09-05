package com.application.lamion.service

import com.application.lamion.model.Request
import com.application.lamion.model.RequestAnalytics
import com.application.lamion.repository.RequestAnalyticsRepository
import com.application.lamion.repository.RequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RequestService @Autowired constructor(
    private val repository: RequestRepository,
    private val analyticsRepository: RequestAnalyticsRepository
) {
    fun save(request: Request): Request {
        return repository.save(request)
    }

    fun findAllByEventId(eventId: Long): List<Request?>? {
        return repository.findAllByEventIdOrderByIdDesc(eventId)
    }

    fun findAnalytics(eventId: Long): List<RequestAnalytics?>? {
        return analyticsRepository.findAllByEventIdOrderByCountDesc(eventId)
    }
}
