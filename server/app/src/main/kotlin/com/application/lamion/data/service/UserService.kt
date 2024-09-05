package com.application.lamion.data.service

import com.application.lamion.data.repository.UserRepository
import com.application.lamion.domain.model.UserDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val repository: UserRepository) {
    fun create(data: UserDomain): UserDomain {
        return repository.save(data)
    }

    fun findByEmail(email: String): UserDomain? {
        return repository.findFirstByEmail(email)
    }

    fun existsByEmail(email: String): Boolean {
        return repository.existsByEmail(email)
    }
}
