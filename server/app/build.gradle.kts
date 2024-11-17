plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(project(":server:shared"))

    // Kotlin
    implementation(libs.kotlin.reflect)

    // Coroutines
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

    // Ktor
    implementation(libs.ktor.core)
    implementation(libs.ktor.java)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serialization.json)

    // Swagger
    implementation(libs.spring.swagger.ui)
    implementation(libs.spring.swagger.api)

    // Jackson
    implementation(libs.jackson.kotlin)
    implementation(libs.jackson.datatype)

    // Other
    implementation(libs.auth0)
}
