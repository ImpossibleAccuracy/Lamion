package com.lamion.feature.auth.data.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.oauth")
data class OauthProperties(
    val github: GitHub,
) {
    data class GitHub(
        val clientId: String,
        val clientSecret: String,
    )
}