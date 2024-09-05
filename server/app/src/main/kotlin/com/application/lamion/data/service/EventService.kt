package com.application.lamion.data.service

import com.application.lamion.data.repository.EventAnalyticsRepository
import com.application.lamion.data.repository.EventRepository
import com.application.lamion.domain.exception.InvalidArgumentsException
import com.application.lamion.domain.exception.ResourceNotFoundException
import com.application.lamion.domain.model.AppDomain
import com.application.lamion.domain.model.EventAnalyticsDomain
import com.application.lamion.domain.model.EventDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventService @Autowired constructor(
    private val repository: EventRepository,
    private val analyticsRepository: EventAnalyticsRepository
) {
    fun create(
        app: AppDomain,
        title: String,
    ): EventDomain {
        if (repository.existsByTitle(title)) {
            throw InvalidArgumentsException("Event with such title already exist")
        }

        return EventDomain(
            title = title,
            date = Date(),
            applicationId = app.id,
        ).let {
            repository.save(it)
        }
    }

    fun getEventByTitleOrCreateNew(
        app: AppDomain,
        title: String,
    ): EventDomain =
        repository
            .findByTitleIgnoreCaseAndApplicationId(title, app.id)
            .orElseGet {
                create(
                    app = app,
                    title = title
                )
            }

    fun getAllAppEvents(app: AppDomain): List<EventAnalyticsDomain> =
        analyticsRepository.findAllByApplicationId(app.id)

    fun getEvent(id: Int, app: AppDomain): EventDomain =
        repository
            .findById(id)
            .orElseThrow {
                throw ResourceNotFoundException()
            }

    fun getEventAnalytics(id: Int, app: AppDomain): EventAnalyticsDomain =
        analyticsRepository
            .findById(id)
            .orElseThrow {
                throw ResourceNotFoundException()
            }

    fun delete(event: EventDomain) {
        repository.delete(event)
    }
}
