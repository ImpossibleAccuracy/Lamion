plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.boot)
}

group = "com.app.lamion"
version = "0.0.1"

dependencies {
    implementation(libs.spring.starter.boot)
    implementation(libs.spring.starter.web)
    implementation(libs.spring.starter.thymeleaf)
    implementation(libs.spring.starter.validation)
    implementation(libs.spring.starter.jpa)
    implementation(libs.spring.starter.actuator)
    developmentOnly(libs.spring.devtools)

    implementation(libs.postgresql.connector)
    implementation(libs.bcrypt)
    implementation(libs.auth0)
}
