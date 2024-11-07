package com.lamion.feature.auth.controller.mapper

import com.lamion.domain.service.resource.ResourceManager
import com.lamion.feature.auth.domain.model.AuthResult
import com.lamion.feature.shared.mapper.toPublicDto

suspend fun AuthResult.toResponse(resourceManager: ResourceManager) =
    com.lamion.feature.auth.controller.payload.response.AuthResponse(
        account = user.toPublicDto(resourceManager),
        token = token,
    )