package com.application.lamion

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LamionApplication

fun main(args: Array<String>) {
    SpringApplication.run(LamionApplication::class.java, *args)
}
