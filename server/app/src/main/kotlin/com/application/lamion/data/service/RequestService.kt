package com.application.lamion.data.service

import com.application.lamion.data.repository.RequestAnalyticsRepository
import com.application.lamion.data.repository.RequestRepository
import com.application.lamion.domain.model.EventDomain
import com.application.lamion.domain.model.RequestAnalyticsDomain
import com.application.lamion.domain.model.RequestDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RequestService @Autowired constructor(
    private val repository: RequestRepository,
    private val analyticsRepository: RequestAnalyticsRepository
) {
    fun create(
        event: EventDomain,
        device: String,
    ): RequestDomain =
        RequestDomain(
            eventId = event.id,
            device = device,
            date = Date(),
        ).let {
            repository.save(it)
        }

    fun getRequests(event: EventDomain): List<RequestDomain> =
        repository.findAllByEventIdOrderByIdDesc(event.id)

    fun findAnalytics(event: EventDomain): List<RequestAnalyticsDomain> =
        analyticsRepository.findAllByEventIdOrderByCountDesc(event.id)
}
