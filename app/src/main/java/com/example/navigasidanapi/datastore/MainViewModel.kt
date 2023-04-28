package com.example.navigasidanapi.datastore

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val settingPreferences = SettingPreferences(application.applicationContext)

    val isDarkModeEnabled = settingPreferences.isDarkModeEnabled.asLiveData()

    fun setIsDarkModeEnabled(isEnabled: Boolean) = viewModelScope.launch {
        settingPreferences.setIsDarkModeEnabled(isEnabled)
    }
}