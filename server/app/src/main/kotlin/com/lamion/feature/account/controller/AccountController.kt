package com.lamion.feature.account.controller

import com.lamion.domain.service.resource.ResourceManager
import com.lamion.feature.account.controller.payload.ProfileResponse
import com.lamion.feature.account.controller.payload.UpdateAccountRequest
import com.lamion.feature.account.domain.service.AccountService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.mapper.toTotalDto
import com.lamion.feature.shared.payload.AccountDto
import com.lamion.server.security.UserHolder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
@SecurityRequirement(name = "jwt")
class AccountController(
    private val accountService: AccountService,
    private val resourceManager: ResourceManager,
) : BaseController() {
    @GetMapping("/me")
    suspend fun me(): AccountDto.Total = endpoint("My account") {
        UserHolder.requireAccount().toTotalDto(resourceManager)
    }

    @GetMapping("/profile")
    suspend fun profile(): ProfileResponse = endpoint("Profile") {
        ProfileResponse(
            account = UserHolder.requireAccount().toTotalDto(resourceManager)
        )
    }

    @PatchMapping("/me")
    suspend fun updateMe(
        @RequestBody @Valid body: UpdateAccountRequest,
    ) = endpoint("Update me") {
        accountService
            .updateAccount(
                account = account,
                email = body.email,
                username = body.username,
            )
            .toTotalDto(resourceManager)
    }
}