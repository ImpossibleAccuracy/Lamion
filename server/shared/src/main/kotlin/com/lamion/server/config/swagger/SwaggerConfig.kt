package com.lamion.server.config.swagger

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.media.Schema
import kotlinx.datetime.*
import kotlinx.datetime.Clock.System.now
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(info = Info(title = "API", version = "v1"))
@SecurityScheme(
    name = "jwt",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
open class SwaggerConfig {
    companion object {
        init {
            val dateTime = now().toLocalDateTime(TimeZone.UTC)

            registerSample<LocalTime>(dateTime.time.format(LocalTime.Formats.ISO))
            registerSample<LocalDate>(dateTime.date.format(LocalDate.Formats.ISO))
            registerSample<LocalDateTime>(dateTime.format(LocalDateTime.Formats.ISO))
        }

        private inline fun <reified T> registerSample(sample: Any) {
            Schema<T>().apply {
                example(sample)
                SpringDocUtils.getConfig().replaceWithSchema(T::class.java, this)
            }
        }
    }
}
