package com.application.lamion.feature.account

import com.application.lamion.domain.service.ResourceManager
import com.application.lamion.feature.account.payload.ProfileResponse
import com.application.lamion.feature.shared.mapper.toTotalDto
import com.application.lamion.feature.shared.payload.AccountDto
import com.application.lamion.server.security.UserHolder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
@SecurityRequirement(name = "jwt")
class AccountController(
    private val resourceManager: ResourceManager,
) {
    @GetMapping("/me")
    suspend fun me(): AccountDto.Total =
        UserHolder.requireAccount().toTotalDto(resourceManager)

    @GetMapping("/profile")
    suspend fun profile(): ProfileResponse =
        ProfileResponse(
            account = UserHolder.requireAccount().toTotalDto(resourceManager)
        )

    // TODO: add update method
}