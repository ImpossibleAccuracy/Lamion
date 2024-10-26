package com.application.lamion.feature.projects.feature.controller.payload.request

import com.application.lamion.domain.model.Id

data class CreateFeatureRequest(
    val title: String,
    val description: String,
    val functions: List<Id>,
)
