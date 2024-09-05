package com.application.lamion.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "application", schema = "public")
class App(
    id: Int? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "description")
    var description: String?,

    @Column(name = "date")
    var date: Date,

    @Column(name = "user_id")
    var userId: Int,
) : BaseModel<Int>(id)
