package com.example.skeleton.Model.Retrofit

import android.util.Log
import com.example.skeleton.Model.Repository.DataStoreRepository
import com.example.skeleton.Model.Retrofit.TokenHandlers.AuthInterceptor
import com.example.skeleton.Model.Retrofit.TokenHandlers.RefreshRetrofit
import com.example.skeleton.Model.Retrofit.TokenHandlers.TokenAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(dataStoreDepo: DataStoreRepository) {
    //HTTPLoggingInterceptor
    val loggingInterceptor = HttpLoggingInterceptor { mess ->
        Log.d("HTTP", mess)
    }
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    val mainOkHttpClient = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor(dataStoreDepo))
//        .authenticator(
//            TokenAuthenticator(dataStoreDepo)
//        )
        .addInterceptor(loggingInterceptor)             //` ← The LoggingInterceptor
        .build()

    var protocol = "http"
    var host = "192.168.29.210"
    var port = 8000
    private var retrofit = buildRetrofit()

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("$protocol://$host:$port/")
            .client(mainOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: APIs
        get() = retrofit.create(APIs::class.java)

    fun setDomain(protocol: String, host: String, port: Int) {
        this.protocol = protocol
        this.host = host
        this.port = port
        retrofit = buildRetrofit()
    }
}
