package com.lamion

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

const val BASE_PACKAGE = "com.lamion"

@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
@ConfigurationPropertiesScan(BASE_PACKAGE)
@EnableWebFlux
class WorkerApplication

fun main(args: Array<String>) {
    runApplication<WorkerApplication>(*args)
}
