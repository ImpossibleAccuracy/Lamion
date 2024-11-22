package com.lamion.feature.auth.data.oauth

import com.fasterxml.jackson.annotation.JsonProperty
import com.lamion.data.network.bearer
import com.lamion.domain.exception.InvalidArgumentsException
import com.lamion.feature.auth.data.model.OauthProperties
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object GithubOauth {
    suspend fun getProfileInfo(
        httpClient: HttpClient,
        code: String,
        properties: OauthProperties.GitHub,
    ): ProfileInfo = try {
        val accessTokenData = httpClient
            .post("https://github.com/login/oauth/access_token") {
                val accessTokenRequest = Token.Request(
                    code = code,
                    clientId = properties.clientId,
                    clientSecret = properties.clientSecret,
                )

                contentType(ContentType.Application.Json)
                setBody(accessTokenRequest)
            }
            .body<Token.Response>()

        val profileInfo = httpClient
            .get("https://api.github.com/user") {
                bearer(accessTokenData.accessToken)
            }
            .body<Profile.Response>()

        val emailsInfo = httpClient
            .get("https://api.github.com/user/emails") {
                bearer(accessTokenData.accessToken)
            }
            .body<List<Profile.EmailItem>>()

        ProfileInfo(
            email = emailsInfo.first { it.primary }.email,
            username = profileInfo.name,
        )
    } catch (t: Throwable) {
        t.printStackTrace()
        throw InvalidArgumentsException("Github verification failed")
    }

    data class ProfileInfo(
        val username: String,
        val email: String,
    )

    object Token {
        data class Request(
            @JsonProperty("code")
            val code: String,

            @JsonProperty("client_id")
            val clientId: String,

            @JsonProperty("client_secret")
            val clientSecret: String,
        )

        data class Response(
            @JsonProperty("access_token")
            val accessToken: String
        )
    }

    object Profile {
        data class Response(
            @JsonProperty("login")
            val name: String,
        )

        data class EmailItem(
            @JsonProperty("email")
            val email: String,

            @JsonProperty("primary")
            val primary: Boolean,
        )
    }
}