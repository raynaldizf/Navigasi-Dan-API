package com.example.navigasidanapi.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigasidanapi.room.FavDatabase
import com.example.navigasidanapi.R
import com.example.navigasidanapi.adapter.AdapterFavorite
import com.example.navigasidanapi.databinding.FragmentFavoritreBinding

class FavoritreFragment : Fragment() {
    private lateinit var binding: FragmentFavoritreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.action_favoritreFragment_to_homeFragment)
        }

        binding.btnSetting.setOnClickListener{
            findNavController().navigate(R.id.action_favoritreFragment_to_settingsFragment)
        }
        binding.rvUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val databaseFavorite = FavDatabase.getInstance(requireContext())
        val listFavorite = databaseFavorite.favDao().getFavorite()
        listFavorite.let{
            databaseFavorite.favDao().getFavorite().observe(viewLifecycleOwner) {
                Handler().postDelayed({
                    requireActivity().runOnUiThread{
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.adapter = AdapterFavorite(it)
                    }
                }, 1000)
            }
        }
    }
}