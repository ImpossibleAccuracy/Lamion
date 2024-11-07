package com.application.lamion

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.web.reactive.config.EnableWebFlux

const val BASE_PACKAGE = "com.application.lamion"

@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
@ConfigurationPropertiesScan(BASE_PACKAGE)
@EnableWebFlux
class LamionApplication

// TODO: add role system
// TODO: add response compression
fun main(args: Array<String>) {
    SpringApplication.run(LamionApplication::class.java, *args)
}
