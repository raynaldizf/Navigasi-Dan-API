package com.example.navigasidanapi.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences(private val context: Context) {
    private object PreferencesKeys {
        val isDarkModeEnabled = booleanPreferencesKey("is_dark_mode_enabled")
    }

    val isDarkModeEnabled: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.isDarkModeEnabled] ?: false
        }

    suspend fun setIsDarkModeEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.isDarkModeEnabled] = isEnabled
        }
    }

    companion object {
        private const val TAG = "SettingPreferences"
    }
}