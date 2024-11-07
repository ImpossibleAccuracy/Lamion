package com.lamion.feature.account.controller

import com.lamion.domain.model.AccountDomain
import com.lamion.domain.model.Id
import com.lamion.feature.account.domain.service.AccountService
import com.lamion.feature.shared.controller.BaseController
import com.lamion.feature.shared.utils.require
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/account/avatar")
@SecurityRequirement(name = "jwt")
class AvatarController(
    private val accountService: AccountService,
    private val fileStorageService: com.lamion.domain.service.file.FileStorageService,
) : BaseController() {
    @GetMapping("/{id}")
    suspend fun accountAvatar(
        @PathVariable id: Id,
    ): ResponseEntity<Flow<DataBuffer>> = endpoint("Account avatar") {
        val targetAccount = accountService
            .getAccount(
                caller = account,
                target = id,
            )
            .require { "Account not found" }

        processAccountAvatar(targetAccount)
    }

    @GetMapping("/me")
    suspend fun me(): ResponseEntity<Flow<DataBuffer>> = endpoint("My avatar") {
        processAccountAvatar(account)
    }

    private suspend fun processAccountAvatar(targetAccount: AccountDomain): ResponseEntity<Flow<DataBuffer>> {
        val avatar = accountService.getAvatar(targetAccount)
            ?: return ResponseEntity.notFound().build()

        val file = fileStorageService.load(avatar.file)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatar.title + "\"")
            .contentType(MediaType.valueOf(avatar.mimeType))
            .body(file)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/me", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun updateAvatar(@RequestPart("file", required = true) multipartFileMono: Mono<FilePart>) =
        endpoint("Avatar update") {
            accountService.updateAvatar(
                account = account,
                file = multipartFileMono.awaitSingle(),
            )
        }
}