package com.lamion.feature.auth.controller.mapper

import com.lamion.domain.service.resource.ResourceManager
import com.lamion.feature.auth.controller.payload.response.AuthResponse
import com.lamion.feature.auth.domain.model.AuthResult

suspend fun AuthResult.toResponse(resourceManager: ResourceManager) =
    AuthResponse(
        id = user.id,
        username = user.username,
        avatar = resourceManager.getAvatarUrl(user),
        token = token,
    )
