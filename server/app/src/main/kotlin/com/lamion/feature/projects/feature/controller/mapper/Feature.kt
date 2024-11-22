package com.lamion.feature.projects.feature.controller.mapper

import com.lamion.feature.projects.feature.domain.model.FeatureDomain
import com.lamion.feature.shared.payload.FeatureDto

fun FeatureDomain.toPartialDto() = FeatureDto.Partial(
    id = id,
    title = title,
    description = description
)

fun FeatureDomain.Detailed.toDto() = FeatureDto.Detailed(
    id = id,
    title = title,
    description = description,
    functionsCount = totalFunctions,
    totalEvents = totalEvents,
    totalErrors = errors,
    topFunction = topFunctions.map {
        FeatureDto.Detailed.TopFunction(
            id = it.id,
            title = it.title,
            totalEvents = it.totalEvents,
            percent = it.percent,
        )
    }
)