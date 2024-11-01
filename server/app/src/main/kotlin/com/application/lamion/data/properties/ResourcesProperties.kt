package com.application.lamion.data.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.resources")
data class ResourcesProperties(
    var resourcesUrl: String,
)
