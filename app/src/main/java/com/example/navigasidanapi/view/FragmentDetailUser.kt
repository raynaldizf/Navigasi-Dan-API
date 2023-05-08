package com.example.navigasidanapi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.navigasidanapi.room.FavDatabase
import com.example.navigasidanapi.room.Favorite
import com.example.navigasidanapi.R
import com.example.navigasidanapi.adapter.FragmentAdapter
import com.example.navigasidanapi.databinding.FragmentDetailUserBinding
import com.example.navigasidanapi.model.UserDetailResponse
import com.example.navigasidanapi.network.RetrofitClient
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentDetailUser : Fragment() {
    lateinit var binding : FragmentDetailUserBinding
    lateinit var adapter : FragmentAdapter
//    private var isEdit = false
//    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailUserBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var nama = arguments?.getString("nama")
        var gambar = arguments?.getString("gambar")
        val data = Bundle()
        data.putString("nama",nama)
        adapter = FragmentAdapter(childFragmentManager,lifecycle,data)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = "Followers"
                1 -> tab.text = "Following"
            }
        }.attach()

        getData(nama!!)

        binding.btnBack.setOnClickListener{
            findNavController().navigateUp()
        }

        val database = FavDatabase.getInstance(requireContext())
        val dao = database.favDao()
        var isFavorited : Int? = 0
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                isFavorited = dao.isFavorited(nama)
                    requireActivity().runOnUiThread {
                    if(isFavorited == 1) binding.btnFav.setImageResource(R.drawable.baseline_love_24)
                    else if(isFavorited == 0 || isFavorited == null) binding.btnFav.setImageResource(R.drawable.baseline_love_24_outline)
                }
            }
        }

        binding.btnFav.setOnClickListener{
            Thread {
                if (isFavorited == 1){
                    val item = Favorite(nama, gambar!!, 0)
                    FavDatabase.getInstance(requireContext()).favDao().deleteFavs(item)
                    binding.btnFav.setImageResource(R.drawable.baseline_love_24_outline)
                    requireActivity().runOnUiThread{
                        Toast.makeText(requireContext(), "Deleted from Favorites", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_fragmentDetailUser_to_favoritreFragment)
                    }
                }else{
                    val item = Favorite(nama, gambar!!, 1)
                    FavDatabase.getInstance(requireContext()).favDao().insertFavs(item)
                    binding.btnFav.setImageResource(R.drawable.baseline_love_24)
                    requireActivity().runOnUiThread{
                        Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_fragmentDetailUser_to_favoritreFragment)
                    }
                }
            }.start()
        }
    }

    private fun getData(userName : String){
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.getApiService().getUserDetail(userName)
            .enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    if (response.isSuccessful){
                        binding.progressBar.visibility = View.GONE
                        binding.txtTwitter.text = response.body()?.login
                        binding.txtNama.text = response.body()?.name
                        Glide.with(requireContext())
                            .load(response.body()?.avatarUrl)
                            .into(binding.images)
                        binding.txtFollower.text = "${response.body()?.followers} Follower"
                        binding.txtFollowing.text = "${response.body()?.following} Following"
                    }else{
                        binding.progressBar.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_LONG).show()
                }
            })
    }
}