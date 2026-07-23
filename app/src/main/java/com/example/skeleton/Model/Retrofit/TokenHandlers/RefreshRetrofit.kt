package com.example.skeleton.Model.Retrofit.TokenHandlers

import com.example.skeleton.Model.Retrofit.APIs
import com.example.skeleton.Model.Retrofit.AuthAPIs
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RefreshRetrofit {
    private const val BASE_URL = "https://official-joke-api.appspot.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthAPIs by lazy {
        retrofit.create(AuthAPIs::class.java)
    }
}