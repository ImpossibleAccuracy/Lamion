package com.lamion.domain.service

import com.lamion.domain.model.Id

interface ProjectService {
    suspend fun findProjectByAccessKey(key: String): Id
}