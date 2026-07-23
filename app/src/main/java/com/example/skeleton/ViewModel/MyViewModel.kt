package com.example.skeleton.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skeleton.Model.KTor.Requests_And_Responses.ChatRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.ChatResponse
import com.example.skeleton.Model.KTor.Requests_And_Responses.CityComparisonResponse
import com.example.skeleton.Model.KTor.Requests_And_Responses.ComplianceEfficacyResponse
import com.example.skeleton.Model.KTor.Requests_And_Responses.EnforcementRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.EnforcementResponse
import com.example.skeleton.Model.KTor.Requests_And_Responses.HealthAdvisoryRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.HealthAdvisoryResponse
import com.example.skeleton.Model.KTor.Requests_And_Responses.PredictRequest
import com.example.skeleton.Model.KTor.Requests_And_Responses.PredictResponse
import com.example.skeleton.Model.Repository.DataStoreRepository
import com.example.skeleton.Model.Repository.NetworkRepository
import com.example.skeleton.Model.Repository.NetworkResult
import com.example.skeleton.Model.Repository.RoomRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class MyViewModel(
    val roomRepo: RoomRepository,
    val networkRepo: NetworkRepository,
    val dataStoreRepo: DataStoreRepository
): ViewModel() {
    //lat and long
    var latitude by mutableStateOf(0.0)
    var longitude by mutableStateOf(0.0)

    var forecast_aqi by mutableStateOf(0.0)
    var current_aqi by mutableStateOf(0.0)
    var traffic_pct by mutableStateOf(0.0)
    var industrial_pct by mutableStateOf(0.0)
    var biomass_pct by mutableStateOf(0.0)

    var predResultObtained by mutableStateOf(false)

    var primaryPollutantSource by mutableStateOf("")

//    the network functions
    private val _predictState =
        MutableStateFlow<NetworkResult<PredictResponse>>(NetworkResult.Idle)
    val predictState: StateFlow<NetworkResult<PredictResponse>> = _predictState.asStateFlow()

    private val _healthAdvisoryState =
        MutableStateFlow<NetworkResult<HealthAdvisoryResponse>>(NetworkResult.Idle)
    val healthAdvisoryState: StateFlow<NetworkResult<HealthAdvisoryResponse>> =
        _healthAdvisoryState.asStateFlow()

    private val _enforcementState =
        MutableStateFlow<NetworkResult<EnforcementResponse>>(NetworkResult.Idle)
    val enforcementState: StateFlow<NetworkResult<EnforcementResponse>> =
        _enforcementState.asStateFlow()

    private val _chatState =
        MutableStateFlow<NetworkResult<ChatResponse>>(NetworkResult.Idle)
    val chatState: StateFlow<NetworkResult<ChatResponse>> = _chatState.asStateFlow()

    private val _cityComparisonState =
        MutableStateFlow<NetworkResult<CityComparisonResponse>>(NetworkResult.Idle)
    val cityComparisonState: StateFlow<NetworkResult<CityComparisonResponse>> =
        _cityComparisonState.asStateFlow()

    private val _complianceEfficacyState =
        MutableStateFlow<NetworkResult<ComplianceEfficacyResponse>>(NetworkResult.Idle)
    val complianceEfficacyState: StateFlow<NetworkResult<ComplianceEfficacyResponse>> =
        _complianceEfficacyState.asStateFlow()

    fun predict(request: PredictRequest) {
        viewModelScope.launch {
            networkRepo.predict(request).collect { result ->
                _predictState.value = result

                if (result is NetworkResult.Success) {
                    updateRoutePoints(result.response.dispersionPlume.geometry.coordinates)
                    forecast_aqi = result.response.forecastPm25.twentyFourHours
                    current_aqi = result.response.forecastPm25.oneHour
                    traffic_pct = result.response.attribution.trafficPct
                    industrial_pct = result.response.attribution.industrialPct
                    biomass_pct = result.response.attribution.biomassPct

                    predResultObtained = true

                    primaryPollutantSource = if (traffic_pct > industrial_pct && traffic_pct > biomass_pct) {
                        "Traffic"
                    } else if (industrial_pct > traffic_pct && industrial_pct > biomass_pct) {
                        "Industrial"
                    } else if (biomass_pct > traffic_pct && biomass_pct > industrial_pct) {
                        "Biomass"
                    } else {
                        "Mixed"
                    }
                }
            }
        }
    }

    fun clearPredictState(){
        _predictState.value = NetworkResult.Idle
    }

    val englishAudioUrl = mutableStateOf("")

    fun getHealthAdvisory(request: HealthAdvisoryRequest) {
        viewModelScope.launch {
            networkRepo.getHealthAdvisory(request).collect { result ->
                _healthAdvisoryState.value = result

                when(result){
                    is NetworkResult.Success -> {
                        englishAudioUrl.value = result.response.ivrAudioUrls.english
                    }

                    else -> {}
                }
            }
        }
    }

    fun clearHealthAdvisoryState() {
        _healthAdvisoryState.value = NetworkResult.Idle
    }

    fun getEnforcement(request: EnforcementRequest) {
        viewModelScope.launch {
            networkRepo.getEnforcement(request).collect { result ->
                _enforcementState.value = result
            }
        }
    }

    fun clearEnforcementState() {
        _enforcementState.value = NetworkResult.Idle
    }

    fun chat(request: ChatRequest) {
        viewModelScope.launch {
            networkRepo.chat(request).collect { result ->
                _chatState.value = result
            }
        }
    }

    fun clearChatState() {
        _chatState.value = NetworkResult.Idle
    }

    fun getCityComparison() {
        viewModelScope.launch {
            networkRepo.getCityComparison().collect { result ->
                _cityComparisonState.value = result
            }
        }
    }

    fun clearCityComparisonState() {
        _cityComparisonState.value = NetworkResult.Idle
    }


    fun getComplianceEfficacy() {
        viewModelScope.launch {
            networkRepo.getComplianceEfficacy().collect { result ->
                _complianceEfficacyState.value = result
            }
        }
    }

    fun clearComplianceEfficacyState() {
        _complianceEfficacyState.value = NetworkResult.Idle
    }


    //Setting the custom domain from UI
    var protocol by mutableStateOf("http")

    fun updateProtocol(text: String){
        protocol = text
    }

    var host by mutableStateOf("192.168.29.210")

    fun updateHost(text: String){
        host = text
    }

    var port by mutableStateOf("8000")

    fun updatePort(text: String){
        port = text
    }

    var isDomainSet by mutableStateOf(false)

    fun setDomain(){
        isDomainSet = true
        networkRepo.setDomain(protocol, host, port.toIntOrNull() ?: 8000)
    }

    //Location Screen
    // Hardcoded User Location: Kolkata (approx)
    private val _userLocation = MutableStateFlow(LatLng(22.5726, 88.3639))
    val userLocation: StateFlow<LatLng> = _userLocation.asStateFlow()

    var isCoordinatesObtained by mutableStateOf(false)

    fun updateUserLocation(latLng: LatLng) {
        isCoordinatesObtained = true
        latitude = latLng.latitude
        longitude = latLng.longitude

        _userLocation.value = latLng
    }

    fun updateRoutePoints(geometryCoordinates: List<List<List<Double>>>) {
        val points = mutableListOf<LatLng>()
        // geometryCoordinates is List<List<List<Double>>> (MultiLineString or Polygon)
        // Outer list: shapes, Middle list: points in shape, Inner list: [lon, lat]
        geometryCoordinates.forEach { shape ->
            shape.forEach { point ->
                if (point.size >= 2) {
                    // GeoJSON format is [longitude, latitude]
                    val lat = point[1]
                    val lon = point[0]
                    points.add(LatLng(lat, lon))
                }
            }
        }
        _routePoints.value = points
    }

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints.asStateFlow()


}
