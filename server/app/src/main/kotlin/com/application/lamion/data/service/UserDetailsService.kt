package com.application.lamion.data.service

import com.application.lamion.data.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val accountRepository: UserRepository
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return accountRepository.findFirstByEmail(username)
            ?: throw UsernameNotFoundException("Account with email: \"$username\" not found")
    }
}