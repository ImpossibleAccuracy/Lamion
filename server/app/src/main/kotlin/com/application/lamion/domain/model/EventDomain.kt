package com.application.lamion.domain.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "event", schema = "public")
class EventDomain(
     id: Int? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "date")
    var date: Date,

    @Column(name = "application_id")
    var applicationId: Int,
) : BaseModel<Int>(id)
