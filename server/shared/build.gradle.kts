plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // Kotlin
    api(libs.kotlin.stdlib)
    api(libs.kotlin.datetime)

    // Coroutines
    api(libs.coroutines.core)

    // Spring
    implementation(libs.spring.starter.boot)
    implementation(libs.spring.starter.web)

    // SWAGGER
    implementation(libs.spring.swagger.api)

    // Database
    api(libs.spring.exposed)
    api(libs.exposed.core)
    api(libs.exposed.jdbc)
    api(libs.exposed.datetime)
    runtimeOnly(libs.postgresql)
}
