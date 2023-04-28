package com.example.navigasidanapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigasidanapi.room.FavRepository
import com.example.navigasidanapi.room.Favorite
import kotlinx.coroutines.launch

class FavoriteViewModel (private val favRepository: FavRepository) : ViewModel() {

    fun getFavorites() = favRepository.getFavorites()

    fun saveFav(favorite: Favorite) {
        viewModelScope.launch {
            favRepository.setFavorite(favorite, 1)
        }
    }

    fun deleteFav(favorite: Favorite) {
        viewModelScope.launch {
            favRepository.setFavorite(favorite, 0)
        }
    }
}