package com.example.navigasidanapi.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.github.com/"
    private const val TOKEN_API = "TOKEN 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"
    private const val AUTH_TOKEN = "Authorization"

    fun getApiService(): RetrofitService {
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader(AUTH_TOKEN, TOKEN_API)
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(RetrofitService::class.java)
    }
}