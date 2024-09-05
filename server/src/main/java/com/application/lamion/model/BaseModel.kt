package com.application.lamion.model

import jakarta.persistence.*
import java.io.Serializable

@MappedSuperclass
abstract class BaseModel<T : Serializable>(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val _id: T?
) {
    val id: T
        get() = _id!!
}