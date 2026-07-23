package com.example.skeleton.Model.KTor.Requests_And_Responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.gson.annotations.SerializedName as GsonName

@Serializable
data class PredictRequest(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class EnforcementRequest(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current_aqi") @GsonName("current_aqi") val currentAqi: Int,
    @SerialName("forecast_aqi") @GsonName("forecast_aqi") val forecastAqi: Int,
    @SerialName("traffic_pct") @GsonName("traffic_pct") val trafficPct: Double = 0.0,
    @SerialName("industrial_pct") @GsonName("industrial_pct") val industrialPct: Double = 0.0,
    @SerialName("biomass_pct") @GsonName("biomass_pct") val biomassPct: Double = 0.0
)

@Serializable
data class ChatRequest(
    val message: String,
    val latitude: Double = 28.6304,
    val longitude: Double = 77.2177
)

@Serializable
data class HealthAdvisoryRequest(
    val latitude: Double,
    val longitude: Double,
    @SerialName("forecast_aqi") @GsonName("forecast_aqi") val forecastAqi: Int,
    @SerialName("primary_pollutant_source") @GsonName("primary_pollutant_source") val primaryPollutantSource: String
)
