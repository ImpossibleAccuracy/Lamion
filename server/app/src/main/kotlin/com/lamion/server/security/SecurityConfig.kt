package com.lamion.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfig {
    @Bean
    fun passwordEncoder(environment: Environment): PasswordEncoder {
        if (environment.activeProfiles.contains("prod")) {
            return BCryptPasswordEncoder()
        }

        @Suppress("DEPRECATION")
        return NoOpPasswordEncoder.getInstance()
    }
}
