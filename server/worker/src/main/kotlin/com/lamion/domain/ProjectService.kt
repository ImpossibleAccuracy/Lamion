package com.lamion.domain

import com.lamion.domain.model.Id

interface ProjectService {
    suspend fun findProjectByAccessKey(key: String): Id
}