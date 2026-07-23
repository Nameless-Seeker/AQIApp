package com.example.skeleton.Model.KTor.Requests_And_Responses

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import com.google.gson.annotations.SerializedName as GsonName

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class PredictResponse(
    val station: String,
    val timestamp: String,
    val attribution: Attribution,
    @SerialName("forecast_pm25") @GsonName("forecast_pm25") val forecastPm25: ForecastPm25,
    @SerialName("dispersion_plume") @GsonName("dispersion_plume") val dispersionPlume: DispersionPlume,
    val location: Location
)

@Serializable
data class Attribution(
    @SerialName("traffic_pct") @GsonName("traffic_pct") val trafficPct: Double,
    @SerialName("industrial_pct") @GsonName("industrial_pct") val industrialPct: Double,
    @SerialName("biomass_pct") @GsonName("biomass_pct") val biomassPct: Double,
    @SerialName("solvent_voc_pct") @GsonName("solvent_voc_pct") val solventVocPct: Double,
    val confidence: Double
)

@Serializable
data class ForecastPm25(
    @SerialName("1h") @GsonName("1h") val oneHour: Double,
    @SerialName("24h") @GsonName("24h") val twentyFourHours: Double,
    @SerialName("48h") @GsonName("48h") val fortyEightHours: Double
)

@Serializable
data class DispersionPlume(
    val type: String,
    val properties: DispersionProperties,
    val geometry: Geometry
)

@Serializable
data class DispersionProperties(
    @SerialName("forecast_aqi") @GsonName("forecast_aqi") val forecastAqi: Double,
    @SerialName("wind_speed_ms") @GsonName("wind_speed_ms") val windSpeedMs: Double,
    @SerialName("wind_direction_deg") @GsonName("wind_direction_deg") val windDirectionDeg: Double,
    @SerialName("risk_level") @GsonName("risk_level") val riskLevel: String
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<List<List<Double>>>
)

@Serializable
data class HealthAdvisoryResponse(
    val location: Location,
    @SerialName("forecast_aqi") @GsonName("forecast_aqi") val forecastAqi: Int,
    @SerialName("vulnerable_locations_found") @GsonName("vulnerable_locations_found") val vulnerableLocationsFound: Int,
    val advisory: Advisory,
    @SerialName("ivr_audio_urls") @GsonName("ivr_audio_urls") val ivrAudioUrls: IvrAudioUrls
)

@Serializable
data class Advisory(
    val english: String,
    val hindi: String,
    val kannada: String
)

@Serializable
data class IvrAudioUrls(
    val english: String,
    val hindi: String
)

@Serializable
data class EnforcementResponse(
    val hotspot: Hotspot,
    @SerialName("top_sources") @GsonName("top_sources") val topSources: List<Source>?,
    val recommendations: List<String>?
)

@Serializable
data class Hotspot(
    @SerialName("current_aqi") @GsonName("current_aqi") val currentAqi: Int,
    @SerialName("forecast_aqi") @GsonName("forecast_aqi") val forecastAqi: Int,
    @SerialName("risk_level") @GsonName("risk_level") val riskLevel: String
)

@Serializable
data class Source(
    val name: String,
    val category: String,
    @SerialName("distance_km") @GsonName("distance_km") val distanceKm: Double,
    val latitude: Double,
    val longitude: Double,
    val score: Double
)

@Serializable
data class ChatResponse(
    val reply: String
)

@Serializable
data class CityComparisonResponse(
    val cities: List<CityData>?
)

@Serializable
data class CityData(
    val city: String,
    @SerialName("current_avg_aqi") @GsonName("current_avg_aqi") val currentAvgAqi: Int,
    @SerialName("primary_pollutant_source") @GsonName("primary_pollutant_source") val primaryPollutantSource: String,
    @SerialName("enforcement_compliance_rate") @GsonName("enforcement_compliance_rate") val enforcementComplianceRate: String,
    val trend: String
)

@Serializable
data class ComplianceEfficacyResponse(
    @SerialName("overall_efficacy_score") @GsonName("overall_efficacy_score") val overallEfficacyScore: Double,
    val metrics: Metrics,
    @SerialName("recent_interventions") @GsonName("recent_interventions") val recentInterventions: List<Intervention>?
)

@Serializable
data class Metrics(
    @SerialName("average_aqi_drop_post_intervention") @GsonName("average_aqi_drop_post_intervention") val averageAqiDropPostIntervention: String,
    @SerialName("most_effective_target") @GsonName("most_effective_target") val mostEffectiveTarget: String,
    @SerialName("least_effective_target") @GsonName("least_effective_target") val leastEffectiveTarget: String
)

@Serializable
data class Intervention(
    val id: String,
    val target: String,
    @SerialName("aqi_before") @GsonName("aqi_before") val aqiBefore: Int,
    @SerialName("aqi_after") @GsonName("aqi_after") val aqiAfter: Int,
    val status: String
)
