package com.example.navigasidanapi.room

import android.content.Context
import com.example.navigasidanapi.network.RetrofitClient
import com.example.navigasidanapi.room.FavDatabase
import com.example.navigasidanapi.room.FavRepository


object Injection {
    fun provideRepository(context: Context): FavRepository {
        val apiService = RetrofitClient.getApiService()
        val database = FavDatabase.getInstance(context)
        val dao = database.favDao()
        return FavRepository.getInstance(apiService, dao)
    }
}