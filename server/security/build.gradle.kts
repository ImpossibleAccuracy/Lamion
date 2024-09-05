plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

group = "com.app.lamion.security"
version = "0.0.1"

dependencies {
    implementation(libs.spring.starter.web)
    implementation(libs.spring.starter.security)
}