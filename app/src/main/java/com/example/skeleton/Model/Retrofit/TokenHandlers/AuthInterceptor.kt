package com.example.skeleton.Model.Retrofit.TokenHandlers

import android.util.Log
import com.example.skeleton.Model.Repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(private val dataStoreRepo: DataStoreRepository)         //` ← Using the TokenStore interface
    : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {    //`Brings the request on board and returns the response for going to the server
        val accessToken =
            runBlocking { dataStoreRepo.getAccessToken().first() }   //`←Fetching the access token
        val requestBuilder = chain.request().newBuilder()   //`← The original request

        Timber.d( accessToken.toString())

        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.header(
                "Authorization",
                "Bearer $accessToken"
            )   //`← Adding the accessToken as a header
        }

        return chain.proceed(requestBuilder.build())    //`←Returning the same updated request
    }
}