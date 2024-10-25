package com.application.lamion.data.database.entity.base

import com.application.lamion.domain.model.Id
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.relational.core.mapping.Column
import java.time.Instant

abstract class BaseAuditEntity(id: Id) : BaseEntity(id) {
    @Column("created_at")
    @CreatedDate
    var createdAt: Instant? = null

    @Column("creator_id")
    @CreatedBy
    var creator: Id? = null
}
