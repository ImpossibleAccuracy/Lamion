package com.application.lamion.repository

import com.application.lamion.model.App
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppRepository : JpaRepository<App?, Long?> {
    fun existsByTitleAndUserId(title: String?, userId: Long): Boolean

    fun existsByIdAndUserId(id: Long, userId: Long): Boolean

    fun countByUserId(userId: Long): Long
}
