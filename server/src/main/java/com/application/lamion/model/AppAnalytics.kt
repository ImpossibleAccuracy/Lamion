package com.application.lamion.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "app_analytics", schema = "public")
class AppAnalytics(
     id: Int? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "description")
    var description: String,

    @Column(name = "date")
    var date: Date,

    @Column(name = "user_id")
    var userId: Int,

    @Column(name = "events_count")
    var eventsCount: Long,

    @Column(name = "requests_count")
    var requestsCount: Long,
) : BaseModel<Int>(id)
