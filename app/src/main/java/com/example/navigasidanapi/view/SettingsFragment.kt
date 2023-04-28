package com.example.navigasidanapi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.navigasidanapi.databinding.FragmentSettingsBinding
import com.example.navigasidanapi.datastore.MainViewModel


class SettingsFragment : Fragment() {
    lateinit var binding : FragmentSettingsBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.isDarkModeEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.darkModeSwitch.isChecked = isEnabled
        }

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIsDarkModeEnabled(isChecked)
        }


    }
}