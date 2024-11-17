package com.lamion.server.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
data class CorsProperties(
    val cors: List<String>
)