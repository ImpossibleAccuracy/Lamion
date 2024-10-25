package com.application.lamion.data.database.repository

import com.application.lamion.data.database.entity.AccountRoleEntity
import com.application.lamion.domain.model.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface AccountRoleRepository : ReactiveCrudRepository<AccountRoleEntity, Id> {
    @Query(
        value = """
            select r.* from "Role" r
            inner join "Role_Account" ra on ra.role_id = r.id
            where ra.account_id = :id
        """
    )
    fun findByAccountId(id: Id): Flux<AccountRoleEntity>
}
