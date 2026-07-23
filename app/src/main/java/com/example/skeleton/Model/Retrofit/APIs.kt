package com.example.skeleton.Model.Retrofit

import com.example.skeleton.Model.KTor.Requests_And_Responses.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIs {
    @POST("api/v1/ml/predict")
    suspend fun predict(@Body request: PredictRequest): PredictResponse

    @POST("api/v1/health/health-advisory")
    suspend fun getHealthAdvisory(@Body request: HealthAdvisoryRequest): HealthAdvisoryResponse

    @POST("api/v1/agents/enforcement")
    suspend fun getEnforcement(@Body request: EnforcementRequest): EnforcementResponse

    @POST("api/v1/agents/chat")
    suspend fun chat(@Body request: ChatRequest): ChatResponse

    @GET("api/v1/analytics/city-comparison")
    suspend fun getCityComparison(): CityComparisonResponse

    @GET("api/v1/analytics/compliance-efficacy")
    suspend fun getComplianceEfficacy(): ComplianceEfficacyResponse
}

interface AuthAPIs {

}
