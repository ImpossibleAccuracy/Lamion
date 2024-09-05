package com.application.lamion.data.service

import com.application.lamion.data.repository.AppAnalyticsRepository
import com.application.lamion.data.repository.AppRepository
import com.application.lamion.domain.exception.OperationRejectedException
import com.application.lamion.domain.exception.ResourceAccessDeniedException
import com.application.lamion.domain.exception.ResourceNotFoundException
import com.application.lamion.domain.model.AppAnalyticsDomain
import com.application.lamion.domain.model.AppDomain
import com.application.lamion.domain.model.UserDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppService @Autowired constructor(
    private val repository: AppRepository,
    private val analyticsRepository: AppAnalyticsRepository
) {
    fun create(
        title: String,
        description: String?,
        user: UserDomain,
    ): AppDomain {
        if (repository.existsByTitleAndUserId(title, user.id)) {
            throw OperationRejectedException("Application with such title already exist.")
        }

        return AppDomain(
            title = title,
            description = description,
            date = Date(),
            userId = user.id,
        ).let {
            repository.save(it)
        }
    }

    fun getUserAppsAnalytics(user: UserDomain): List<AppAnalyticsDomain> =
        analyticsRepository.findAllByUserId(user.id)

    fun countUserApps(user: UserDomain): Long =
        repository.countByUserId(user.id)

    fun getApp(id: Int, user: UserDomain): AppDomain {
        if (!checkUserHasAccessToApp(id, user)) {
            throw ResourceAccessDeniedException("Access denied")
        }

        return repository.findById(id).orElseThrow {
            ResourceNotFoundException()
        }
    }

    fun getAppUnsecured(id: Int): AppDomain =
        repository.findById(id).orElseThrow {
            ResourceNotFoundException()
        }

    fun getAppAnalytics(id: Int, user: UserDomain): AppAnalyticsDomain {
        if (!checkUserHasAccessToApp(id, user)) {
            throw ResourceAccessDeniedException("Access denied")
        }

        return analyticsRepository.findById(id).orElseThrow {
            ResourceNotFoundException()
        }
    }

    fun update(
        app: AppDomain,
        title: String,
        description: String?,
    ): AppDomain = AppDomain(
        id = app.id,
        title = title,
        description = description,
        date = app.date,
        userId = app.userId,
    ).let {
        repository.save(it)
    }

    fun delete(app: AppDomain) {
        repository.delete(app)
    }

    private fun checkUserHasAccessToApp(id: Int, user: UserDomain): Boolean {
        return repository.existsByIdAndUserId(id, user.id)
    }
}
