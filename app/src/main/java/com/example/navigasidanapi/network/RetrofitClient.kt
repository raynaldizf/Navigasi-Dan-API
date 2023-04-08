package com.example.navigasidanapi.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
//    private const val BASE_URL = "https://api.github.com/"

//    val instance : RetrofitService by lazy {
//        val service = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//        service.create(RetrofitService::class.java)
//    }

    private const val BASE_URL = "https://api.github.com/"
    private const val TOKEN_API = "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"
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