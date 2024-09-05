package com.application.lamion.data.repository

import com.application.lamion.domain.model.UserDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserDomain, Long> {
    fun findFirstByEmail(email: String): UserDomain?
    fun existsByEmail(email: String): Boolean
}
