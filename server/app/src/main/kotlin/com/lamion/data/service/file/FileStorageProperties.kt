package com.lamion.data.service.file

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "app.storage")
data class FileStorageProperties(
    val storePath: String,
    val fileNamePattern: String,
)
