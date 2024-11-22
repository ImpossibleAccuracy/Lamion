package com.lamion.server.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lamion.server.properties.CorsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class HttpConfig : WebFluxConfigurer {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        val mapper = objectMapper()

        configurer.defaultCodecs().jackson2JsonEncoder(
            Jackson2JsonEncoder(mapper)
        )

        configurer.defaultCodecs().jackson2JsonDecoder(
            Jackson2JsonDecoder(mapper)
        )
    }

    @Bean
    fun filterChain(
        http: ServerHttpSecurity,
        authenticationManager: ReactiveAuthenticationManager,
        authenticationRepository: ServerSecurityContextRepository,
    ): SecurityWebFilterChain =
        http.authenticationManager(authenticationManager)
            .securityContextRepository(authenticationRepository)
            .csrf { it.disable() }
            .authorizeExchange {
                it.anyExchange().permitAll()
            }
            .build()


    @Bean
    fun corsFilter(
        corsProperties: CorsProperties,
    ): CorsWebFilter = CorsConfiguration().run {
        allowCredentials = true

        addAllowedHeader("*")
        addAllowedMethod("*")
        corsProperties.cors.forEach {
            addAllowedOrigin(it)
        }

        CorsWebFilter(
            UrlBasedCorsConfigurationSource().apply {
                registerCorsConfiguration("/**", this@run)
            }
        )
    }
}