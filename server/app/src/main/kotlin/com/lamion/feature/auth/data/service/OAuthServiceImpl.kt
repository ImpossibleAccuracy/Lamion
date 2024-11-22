package com.lamion.feature.auth.data.service

import com.lamion.feature.auth.data.model.OauthProperties
import com.lamion.feature.auth.data.oauth.GithubOauth
import com.lamion.feature.auth.domain.model.AuthResult
import com.lamion.feature.auth.domain.service.AuthService
import com.lamion.feature.auth.domain.service.OAuthService
import io.ktor.client.*
import org.springframework.stereotype.Service

@Service
class OAuthServiceImpl(
    private val httpClient: HttpClient,
    private val authService: AuthService,
    private val oauthProperties: OauthProperties,
) : OAuthService {
    override suspend fun authWithGithub(code: String): AuthResult {
        val profile = GithubOauth.getProfileInfo(
            httpClient = httpClient,
            code = code,
            properties = oauthProperties.github,
        )

        return authService.createUserWithOauth(
            email = profile.email,
            username = profile.username,
        )
    }
}