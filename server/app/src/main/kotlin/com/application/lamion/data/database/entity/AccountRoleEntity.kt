package com.application.lamion.data.database.entity

import com.application.lamion.data.database.entity.base.BaseEntity
import com.application.lamion.domain.model.AccountRole
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "\"Role\"")
class AccountRoleEntity(
    @Column("title")
    val enum: AccountRole
) : BaseEntity()
