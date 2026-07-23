package com.example.skeleton.Model.Repository

import com.example.skeleton.Model.KTor.APIs
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
import com.example.skeleton.Model.Retrofit.RetrofitClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkRepository(private val retrofit: RetrofitClient) {
    fun predict(request: PredictRequest): Flow<NetworkResult<PredictResponse>> = safeApiCall {
        retrofit.api.predict(request)
    }

    fun getHealthAdvisory(
        request: HealthAdvisoryRequest
    ): Flow<NetworkResult<HealthAdvisoryResponse>> = safeApiCall {
        retrofit.api.getHealthAdvisory(request)
    }

    fun getEnforcement(request: EnforcementRequest): Flow<NetworkResult<EnforcementResponse>> =
        safeApiCall {
            retrofit.api.getEnforcement(request)
        }

    fun chat(request: ChatRequest): Flow<NetworkResult<ChatResponse>> = safeApiCall {
        retrofit.api.chat(request)
    }

    fun getCityComparison(): Flow<NetworkResult<CityComparisonResponse>> = safeApiCall {
        retrofit.api.getCityComparison()
    }

    fun getComplianceEfficacy(): Flow<NetworkResult<ComplianceEfficacyResponse>> = safeApiCall {
        retrofit.api.getComplianceEfficacy()
    }

    fun setDomain(protocol: String, host: String, port: Int) {
        Timber.d(host)
        retrofit.setDomain(protocol,host,port)

    }

    private fun <T> safeApiCall(apiCall: suspend () -> T): Flow<NetworkResult<T>> = flow {
        emit(NetworkResult.Loading)

        try {
            emit(NetworkResult.Success(apiCall()))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: RedirectResponseException) {
            emit(exception.toNetworkError(NetworkErrorType.Redirect))
        } catch (exception: ClientRequestException) {
            emit(exception.toNetworkError(NetworkErrorType.Client))
        } catch (exception: ServerResponseException) {
            emit(exception.toNetworkError(NetworkErrorType.Server))
        } catch (exception: ResponseException) {
            emit(exception.toNetworkError(NetworkErrorType.Http))
        } catch (exception: HttpRequestTimeoutException) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.Timeout,
                    message = exception.message ?: "Request timed out",
                    throwable = exception
                )
            )
        } catch (exception: SocketTimeoutException) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.Timeout,
                    message = exception.message ?: "Request timed out",
                    throwable = exception
                )
            )
        } catch (exception: UnknownHostException) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.NoInternet,
                    message = exception.message ?: "Unable to reach server",
                    throwable = exception
                )
            )
        } catch (exception: ConnectException) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.NoInternet,
                    message = exception.message ?: "Unable to connect to server",
                    throwable = exception
                )
            )
        } catch (exception: SerializationException) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.Serialization,
                    message = exception.message ?: "Unable to read server response",
                    throwable = exception
                )
            )
        } catch (exception: IOException) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.Network,
                    message = exception.message ?: "Network request failed",
                    throwable = exception
                )
            )
        } catch (exception: Exception) {
            emit(
                NetworkResult.Error(
                    type = NetworkErrorType.Unknown,
                    message = exception.message ?: "Something went wrong",
                    throwable = exception
                )
            )
        }
    }

    private fun ResponseException.toNetworkError(type: NetworkErrorType): NetworkResult.Error {
        return NetworkResult.Error(
            type = type,
            message = response.status.description,
            statusCode = response.status.value,
            throwable = this
        )
    }
}

sealed interface NetworkResult<out T> {
    data object Loading : NetworkResult<Nothing>

    data object Idle : NetworkResult<Nothing>

    data class Success<T>(
        val response: T
    ) : NetworkResult<T>

    data class Error(
        val type: NetworkErrorType,
        val message: String,
        val statusCode: Int? = null,
        val throwable: Throwable? = null
    ) : NetworkResult<Nothing>
}

enum class NetworkErrorType {
    Redirect,
    Client,
    Server,
    Http,
    Timeout,
    NoInternet,
    Serialization,
    Network,
    Unknown
}
