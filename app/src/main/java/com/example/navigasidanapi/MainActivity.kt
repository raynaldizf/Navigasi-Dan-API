package com.example.navigasidanapi

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.navigasidanapi.databinding.ActivityMainBinding
import com.example.navigasidanapi.datastore.PreferenceViewModel
import com.example.navigasidanapi.datastore.SettingPreferences
import com.example.navigasidanapi.datastore.ViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val pref = SettingPreferences.getInstance(dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getThemeSettings().observe(this) { darkActive : Boolean ->
            if (darkActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(binding.root)


    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.fragmentContainerView)
        if (navController.currentDestination?.id == R.id.homeFragment) {
            // Do nothing
            // Supaya tidak bisa back
        } else {
            super.onBackPressed()
        }
    }
}