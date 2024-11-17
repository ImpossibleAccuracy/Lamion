package com.lamion.feature.shared.mapper

import com.lamion.feature.projects.settings.domain.model.AccessKeyDomain
import com.lamion.feature.shared.payload.AccessKeysDto
import kotlinx.datetime.toJavaLocalDate

fun AccessKeyDomain.toDto() = AccessKeysDto(
    id = id,
    title = title,
    createdAt = createdAt.toJavaLocalDate(),
)
