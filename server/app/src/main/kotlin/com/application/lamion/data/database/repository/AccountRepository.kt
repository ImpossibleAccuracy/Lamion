package com.application.lamion.data.database.repository

import com.application.lamion.data.database.entity.AccountEntity
import com.application.lamion.domain.model.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AccountRepository : ReactiveCrudRepository<AccountEntity, Id> {
    fun findFirstByEmail(email: String): Mono<AccountEntity>
    fun existsByEmail(email: String): Mono<Boolean>
}
