package com.application.lamion.model

import jakarta.persistence.*

@Entity
@Table(name = "user", schema = "public")
class User(
     id: Int? = null,

    @Column(name = "email")
    var email: String,

    @Column(name = "password")
    var password: String,
) : BaseModel<Int>(id)
