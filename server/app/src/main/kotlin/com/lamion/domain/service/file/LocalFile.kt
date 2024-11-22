package com.lamion.domain.service.file

import com.lamion.domain.model.Id
import java.io.File

data class LocalFile(
    val id: Id,
    val title: String,
    val mimeType: String,
    val file: File,
)
