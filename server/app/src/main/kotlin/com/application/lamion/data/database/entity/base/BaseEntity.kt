package com.application.lamion.data.database.entity.base

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import com.application.lamion.domain.model.Id as IdDomain

abstract class BaseEntity(
    @Suppress("PropertyName")
    @field:Id
    @field:Column("id")
    var _id: IdDomain? = null
) {
    val id: IdDomain
        get() = _id!!
}
