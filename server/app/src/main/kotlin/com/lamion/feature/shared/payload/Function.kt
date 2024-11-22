package com.lamion.feature.shared.payload

import com.lamion.feature.projects.feature.controller.mapper.toPartialDto
import com.lamion.feature.projects.function.domain.FunctionDomain

fun FunctionDomain.toPartialDto() = FunctionDto.Partial(
    id = id,
    title = title,
)

fun FunctionDomain.Detailed.toDto() = FunctionDto.Detailed(
    id = id,
    title = title,
    events = events,
    features = features.map { it.toPartialDto() },
    tags = tags.map {
        FunctionDto.Detailed.Tag(
            id = it.id,
            title = it.title,
        )
    }
)
