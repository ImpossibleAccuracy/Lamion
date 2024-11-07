package com.lamion.feature.project.controller.mapper

import com.lamion.domain.model.ProjectDomain
import com.lamion.feature.shared.payload.ProjectDto

fun ProjectDomain.toDto() = ProjectDto(
    id = id,
    title = title,
    description = description,
)