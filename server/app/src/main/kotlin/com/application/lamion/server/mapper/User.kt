package com.application.lamion.server.mapper

import com.application.lamion.domain.model.UserDomain
import com.application.lamion.server.payload.dto.UserDto

fun UserDomain.toDto() = UserDto(
    id = id,
    email = email
)