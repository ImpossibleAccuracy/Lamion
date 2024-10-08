package com.application.lamion.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "request_analytics", schema = "public")
class RequestAnalyticsDomain(
    id: Int? = null,

    @Column(name = "device")
    var device: String,

    @Column(name = "count")
    var count: Int,

    @Column(name = "event_id")
    var eventId: Int,
) : BaseModel<Int>(id)
