package com.application.lamion.domain.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "request", schema = "public")
class RequestDomain(
     id: Int? = null,

    @Column(name = "event_id")
    var eventId: Int,

    @Column(name = "device")
    var device: String,

    @Column(name = "date")
    var date: Date,
) : BaseModel<Int>(id)
