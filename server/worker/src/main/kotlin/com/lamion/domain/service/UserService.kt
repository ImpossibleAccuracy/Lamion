package com.lamion.domain.service

import com.lamion.domain.model.Id

interface UserService {
    suspend fun findOrCreateUser(
        projectId: Id,
        deviceKey: String?,
        clientKey: String?,
    ): Id
}