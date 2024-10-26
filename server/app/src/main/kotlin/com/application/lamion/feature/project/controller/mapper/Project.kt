package com.application.lamion.feature.project.controller.mapper

import com.application.lamion.domain.model.ProjectDomain
import com.application.lamion.feature.shared.payload.dto.ProjectDto

fun ProjectDomain.toDto() = ProjectDto(
    id = id,
    title = title,
    description = description,
)