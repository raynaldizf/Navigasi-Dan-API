package com.example.navigasidanapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.navigasidanapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.fragmentContainerView)
        if (navController.currentDestination?.id == R.id.homeFragment) {
            // Do nothing
        } else {
            super.onBackPressed()
        }
    }
}