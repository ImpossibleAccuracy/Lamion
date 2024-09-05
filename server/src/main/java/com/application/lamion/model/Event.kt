package com.application.lamion.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "event", schema = "public")
class Event(
     id: Int? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "date")
    var date: Date,

    @Column(name = "application_id")
    var applicationId: Int,
) : BaseModel<Int>(id)
