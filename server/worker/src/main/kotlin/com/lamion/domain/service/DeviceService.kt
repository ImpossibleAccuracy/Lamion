package com.lamion.domain.service

import com.lamion.domain.model.Id

interface DeviceService {
    suspend fun findOrCreateDevice(
        name: String,
        platform: String,
    ): Id
}