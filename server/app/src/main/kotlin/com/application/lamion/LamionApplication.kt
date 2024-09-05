package com.application.lamion

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(scanBasePackages = ["com.application.lamion"])
@ConfigurationPropertiesScan("com.application.lamion")
class LamionApplication

fun main(args: Array<String>) {
    SpringApplication.run(LamionApplication::class.java, *args)
}
