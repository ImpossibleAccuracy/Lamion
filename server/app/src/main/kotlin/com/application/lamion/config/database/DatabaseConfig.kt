package com.application.lamion.config.database

import com.application.lamion.domain.model.Authorization
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.security.core.context.ReactiveSecurityContextHolder

@Configuration
@EnableR2dbcAuditing
class DatabaseConfig {
    @Bean
    @ConditionalOnMissingBean
    fun auditorAware(): ReactiveAuditorAware<Long?> {
        return ReactiveAuditorAware {
            ReactiveSecurityContextHolder.getContext()
                .map { it.authentication }
                .filter { it.isAuthenticated }
                .map { it.principal as Authorization }
                .map { it.account.id }
        }
    }
}
