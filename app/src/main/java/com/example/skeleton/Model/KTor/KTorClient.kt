package com.example.skeleton.Model.KTor

import com.example.skeleton.Model.Repository.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.IOException


//object tokens {
//    fun getAccessToken(): String = "" // TODO: Implement token retrieval logic
//}

@Serializable
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
)

class KtorClient(val tokens: DataStoreRepository) {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTP
                host = "192.168.29.210"
                port = 8000
            }

            header("Accept", "application/json")
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = tokens.getAccessToken().firstOrNull()
                            ?: return@loadTokens null,
                        refreshToken = tokens.getRefreshToken().firstOrNull() ?: ""
                    )
                }

                refreshTokens {
                    val refreshToken = tokens.getRefreshToken().first()

                    try {
                        val result = refreshClient.refClient.post {
                            url {
                                path("refresh")
                            }
                            header("Authorization", "Bearer $refreshToken")
                        }

                        val newTokens = result.body<RefreshResponse>()

                        tokens.saveSession(
                            accessToken = newTokens.accessToken,
                            refreshToken = newTokens.refreshToken
                        )

                        BearerTokens(newTokens.accessToken, newTokens.refreshToken)
                    }

                    catch(e: Exception){
                        null
                    }
                }
            }
        }
    }
}

object refreshClient {
    val refClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(Logging){
            level = LogLevel.ALL
        }

        install(DefaultRequest){
            url {
                protocol = URLProtocol.HTTPS

                host = "some.thing"
            }
        }
    }
}