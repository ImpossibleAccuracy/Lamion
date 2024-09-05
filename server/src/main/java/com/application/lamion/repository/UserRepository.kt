package com.application.lamion.repository

import com.application.lamion.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun findFirstByEmail(email: String?): User?
    fun existsByEmail(email: String?): Boolean
}
