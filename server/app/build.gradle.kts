plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("org.springframework.data.relational.core.mapping.Table")
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)

    // Spring
    implementation(libs.spring.starter.boot)
    implementation(libs.spring.starter.validation)
    implementation(libs.spring.starter.security)
    implementation(libs.spring.starter.actuator)
    developmentOnly(libs.spring.devtools)

    // Web server
    implementation(libs.spring.starter.web)
    implementation(libs.netty.core)
    implementation(libs.netty.http)
    implementation(libs.reactor.kotlin)

    // Swagger
    implementation(libs.spring.swagger.ui)
    implementation(libs.spring.swagger.api)

    // Database
    implementation(libs.spring.r2dbc)
    implementation(libs.r2dbc.pool)
    runtimeOnly(libs.postgresql)
    runtimeOnly(libs.postgresql.r2dbc)

    // Other
    implementation(libs.auth0)
    implementation(libs.jackson)
}
