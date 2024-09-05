package com.application.lamion.data.repository

import com.application.lamion.domain.model.AppDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppRepository : JpaRepository<AppDomain, Int> {
    fun existsByTitleAndUserId(title: String, userId: Int): Boolean

    fun existsByIdAndUserId(id: Int, userId: Int): Boolean

    fun countByUserId(userId: Int): Long
}
