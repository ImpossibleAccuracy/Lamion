package com.application.lamion.data.database.entity

import com.application.lamion.data.database.entity.base.BaseEntity
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "\"Account\"")
data class AccountEntity(
    @Column("email")
    val email: String,

    @Column("username")
    val username: String,

    @Column("password")
    val password: String,
) : BaseEntity()
