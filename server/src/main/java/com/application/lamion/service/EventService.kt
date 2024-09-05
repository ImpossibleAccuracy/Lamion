package com.application.lamion.service

import com.application.lamion.model.Event
import com.application.lamion.model.EventAnalytics
import com.application.lamion.repository.EventAnalyticsRepository
import com.application.lamion.repository.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EventService @Autowired constructor(
    private val repository: EventRepository,
    private val analyticsRepository: EventAnalyticsRepository
) {
    fun create(data: Event): Event {
        return repository.save(data)
    }


    fun findAllByApplicationId(appId: Long): List<EventAnalytics?>? {
        return analyticsRepository.findAllByApplicationId(appId)
    }

    fun find(id: Long): EventAnalytics? {
        return analyticsRepository.findById(id).orElse(null)
    }

    fun findByTitleAndApplicationId(title: String?, appId: Long): Event? {
        return repository.findByTitleAndApplicationId(title, appId)
    }

    fun exists(id: Long): Boolean {
        return repository.existsById(id)
    }

    fun existsByTitle(title: String?): Boolean {
        return repository.existsByTitle(title)
    }


    fun update(data: Event) {
        repository.save(data)
    }

    fun delete(id: Long) {
        repository.deleteById(id)
    }
}
