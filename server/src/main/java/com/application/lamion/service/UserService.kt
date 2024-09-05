package com.application.lamion.service

import com.application.lamion.model.User
import com.application.lamion.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(private val repository: UserRepository) {
    fun create(data: User): User {
        return repository.save(data)
    }

    fun get(): List<User?> {
        return repository.findAll()
    }

    fun find(id: Long): User? {
        return repository.findById(id).orElse(null)
    }

    fun findByEmail(email: String?): User? {
        return repository.findFirstByEmail(email)
    }

    fun exists(id: Long): Boolean {
        return repository.existsById(id)
    }

    fun existsByEmail(email: String?): Boolean {
        return repository.existsByEmail(email)
    }

    fun update(data: User) {
        repository.saveAndFlush(data)
    }

    fun delete(id: Long) {
        repository.deleteById(id)
    }
}
