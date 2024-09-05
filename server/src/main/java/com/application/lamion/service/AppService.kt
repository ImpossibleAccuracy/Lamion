package com.application.lamion.service

import com.application.lamion.model.App
import com.application.lamion.model.AppAnalytics
import com.application.lamion.repository.AppAnalyticsRepository
import com.application.lamion.repository.AppRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AppService @Autowired constructor(
    private val repository: AppRepository,
    private val analyticsRepository: AppAnalyticsRepository
) {
    // CREATE
    fun create(data: App): App {
        return repository.save(data)
    }

    // READ
    fun get(userID: Long): List<AppAnalytics?>? {
        return analyticsRepository.findAllByUserId(userID)
    }

    fun count(userId: Long): Long {
        return repository.countByUserId(userId)
    }

    fun find(id: Long): AppAnalytics? {
        return analyticsRepository.findById(id).orElse(null)
    }

    fun exists(id: Long): Boolean {
        return repository.existsById(id)
    }

    fun existsByTitleAndUserId(title: String?, userID: Long): Boolean {
        return repository.existsByTitleAndUserId(title, userID)
    }

    fun hasAccess(id: Long, userId: Long): Boolean {
        return repository.existsByIdAndUserId(id, userId)
    }

    // UPDATE
    fun update(data: App) {
        repository.save(data)
    }

    fun updateTitle(id: Long, title: String, description: String?): App? {
        val app = repository.findById(id).orElse(null) ?: return null
        app.title = title
        app.description = description
        return repository.save(app)
    }

    // DELETE
    fun delete(id: Long) {
        repository.deleteById(id)
    }
}
