package com.application.lamion.data.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.token")
data class TokenProperties(
    var secret: String,
    var issuer: String,
    var audience: String,
    var ttl: Long,
)
