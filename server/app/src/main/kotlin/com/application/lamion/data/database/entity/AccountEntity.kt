package com.application.lamion.data.database.entity

import com.application.lamion.data.database.entity.base.BaseEntity
import com.application.lamion.domain.model.AccountDomain
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "\"Account\"")
data class AccountEntity(
    @Column("email")
    override val email: String,

    @Column("username")
    override val username: String,

    @Column("password")
    val password: String,
) : AccountDomain, BaseEntity()
