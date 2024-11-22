package com.lamion.data.network

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtorConfiguration {
    @Bean
    fun ktor(mapper: ObjectMapper) = HttpClient(Java) {
        install(ContentNegotiation) {
            val converter = JacksonConverter(mapper, true)
            register(ContentType.Application.Json, converter)
        }
    }
}

fun HttpMessageBuilder.bearer(bearer: String) =
    header("Authorization", "Bearer $bearer")
