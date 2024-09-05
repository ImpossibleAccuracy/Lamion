package com.application.lamion.domain.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.token")
data class TokenProperties(
    var secret: String,
    var issuer: String,
    var audience: String,
    var claimName: String,
    var ttl: Long,
)
