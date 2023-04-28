package com.example.navigasidanapi.room

import androidx.lifecycle.LiveData
import com.example.navigasidanapi.network.RetrofitService

class FavRepository private constructor(private val favDao: FavoriteDao) {

    fun getFavorites(): LiveData<List<Favorite>> {
        return favDao.getFavorite()
    }

    suspend fun setFavorite(favs: Favorite, isFav: Int) {
        favs.is_favorite = isFav
        favDao.updateFavs(favs)
    }

    companion object {
        @Volatile
        private var instance: FavRepository? = null
        fun getInstance(
            apiService: RetrofitService,
            favsDao: FavoriteDao
        ): FavRepository =
            instance ?: synchronized(this) {
                instance ?: FavRepository(favsDao)
            }.also { instance = it }
    }
}