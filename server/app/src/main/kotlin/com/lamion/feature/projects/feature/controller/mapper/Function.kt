package com.lamion.feature.projects.feature.controller.mapper

import com.lamion.feature.projects.feature.domain.model.FunctionDomain
import com.lamion.feature.shared.payload.FunctionDto

fun FunctionDomain.toPartialDto() = FunctionDto.Partial(
    id = id,
    title = title,
)

fun FunctionDomain.Detailed.toDto() = FunctionDto.Detailed(
    id = id,
    title = title,
    totalEvents = totalEvents,
    features = features.map { it.toPartialDto() },
    tags = tags.map {
        FunctionDto.Detailed.Tag(
            id = it.id,
            title = it.title,
        )
    }
)
