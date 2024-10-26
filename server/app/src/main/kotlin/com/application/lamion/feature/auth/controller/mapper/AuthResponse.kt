package com.application.lamion.feature.auth.controller.mapper

import com.application.lamion.domain.service.ResourceManager
import com.application.lamion.feature.auth.controller.payload.response.AuthResponse
import com.application.lamion.feature.auth.domain.model.AuthResult
import com.application.lamion.feature.shared.mapper.toPublicDto

suspend fun AuthResult.toResponse(resourceManager: ResourceManager) = AuthResponse(
    account = user.toPublicDto(resourceManager),
    token = token,
)