package com.example.skeleton.Model.KTor

import com.example.skeleton.Model.KTor.Requests_And_Responses.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import timber.log.Timber

class APIs(val client: HttpClient) {
    suspend fun predict(request: PredictRequest): PredictResponse {
        val result = client.post {
            url {
                path("api", "v1", "ml", "predict")
            }
            setBody(request)
        }

        Timber.d("Response status: ${result.status}")
        val responseBody = result.body<PredictResponse>()
        Timber.d("Prediction received: $responseBody")
        return responseBody
    }

    suspend fun getHealthAdvisory(request: HealthAdvisoryRequest): HealthAdvisoryResponse {
        val result = client.post {
            url {
                path("api", "v1", "health-advisory")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return result.body()
    }

    suspend fun getEnforcement(request: EnforcementRequest): EnforcementResponse {
        val result = client.post {
            url {
                path("api", "v1","agents", "enforcement")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return result.body()
    }

    suspend fun chat(request: ChatRequest): ChatResponse {
        val result = client.post {
            url {
                path("api", "v1","agents", "chat")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return result.body()
    }

    suspend fun getCityComparison(): CityComparisonResponse {
        val result = client.get {
            url {
                path("api", "v1","analytics", "city-comparison")
            }
        }
        return result.body()
    }

    suspend fun getComplianceEfficacy(): ComplianceEfficacyResponse {
        val result = client.get {
            url {
                path("api", "v1", "analytics", "compliance-efficacy")
            }
        }
        return result.body()
    }
}
