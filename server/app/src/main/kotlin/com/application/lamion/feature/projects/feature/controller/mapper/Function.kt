package com.application.lamion.feature.projects.feature.controller.mapper

import com.application.lamion.feature.projects.feature.domain.model.FunctionDomain
import com.application.lamion.feature.shared.payload.dto.FunctionDto

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
