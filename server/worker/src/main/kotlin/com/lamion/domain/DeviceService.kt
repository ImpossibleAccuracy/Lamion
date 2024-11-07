package com.lamion.domain

import com.lamion.domain.model.Id

interface DeviceService {
    suspend fun findOrCreateDevice(
        name: String,
        platform: String,
    ): Id
}