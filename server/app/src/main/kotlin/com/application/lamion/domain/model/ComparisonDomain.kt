package com.application.lamion.domain.model

data class ComparisonDomain<T>(
    val actual: T,
    val past: T,
)