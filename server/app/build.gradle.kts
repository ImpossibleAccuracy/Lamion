plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.allopen)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
    annotation("com.application.lamion.server.payload.dto.DTO")
}

group = "com.application.lamion"
version = "0.0.1"

dependencies {
    implementation(project(":server:security"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    implementation(libs.spring.starter.boot)
    implementation(libs.spring.starter.web)
    implementation(libs.spring.starter.thymeleaf)
    implementation(libs.spring.starter.validation)
    implementation(libs.spring.starter.jpa)
    implementation(libs.spring.starter.security)
    implementation(libs.spring.starter.actuator)
    developmentOnly(libs.spring.devtools)

    implementation(libs.spring.swagger.ui)
    implementation(libs.spring.swagger.api)

    implementation(libs.postgresql.connector)
    implementation(libs.bcrypt)
    implementation(libs.auth0)
}
