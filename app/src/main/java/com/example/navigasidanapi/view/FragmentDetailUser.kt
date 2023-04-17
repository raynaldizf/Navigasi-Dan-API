package com.example.navigasidanapi.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.navigasidanapi.R
import com.example.navigasidanapi.adapter.AdapterFollowers
import com.example.navigasidanapi.adapter.AdapterFollowing
import com.example.navigasidanapi.adapter.FragmentAdapter
import com.example.navigasidanapi.databinding.FragmentDetailUserBinding
import com.example.navigasidanapi.model.ResponseUserFollowerItem
import com.example.navigasidanapi.model.ResponseUserFollowingItem
import com.example.navigasidanapi.model.UserDetailResponse
import com.example.navigasidanapi.network.RetrofitClient
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentDetailUser : Fragment() {
    lateinit var binding : FragmentDetailUserBinding
    lateinit var adapter : FragmentAdapter
    lateinit var _adapter : AdapterFollowers
    lateinit var adapter_ : AdapterFollowing

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
      //  (activity as AppCompatActivity).setSupportActionBar(toolbar_name)

        var nama = arguments?.getString("nama")
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

    }

    private fun getData(userName : String){
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.getApiService().getUserDetail(userName)
            .enqueue(object : Callback<UserDetailResponse> {
                @SuppressLint("SetTextI18n")
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

                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Something Wrong", Toast.LENGTH_LONG).show()
                }
            })
    }


}