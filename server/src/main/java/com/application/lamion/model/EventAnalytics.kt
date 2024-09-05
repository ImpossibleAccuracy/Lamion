package com.application.lamion.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "event_analytics", schema = "public")
class EventAnalytics(
     id: Int? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "date")
    var date: Date,

    @Column(name = "application_id")
    var applicationId: Int,

    @Column(name = "requests_count")
    var requestsCount: Int,

    @Column(name = "most_used_device")
    var mostUsedDevice: String,

    @Column(name = "high_demand_time")
    var highDemandTime: String,
) : BaseModel<Int>(id)
