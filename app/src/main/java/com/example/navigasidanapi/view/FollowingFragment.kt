package com.example.navigasidanapi.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigasidanapi.adapter.AdapterFollowers
import com.example.navigasidanapi.adapter.AdapterFollowing
import com.example.navigasidanapi.databinding.FragmentFollowingBinding
import com.example.navigasidanapi.model.ResponseUserFollowerItem
import com.example.navigasidanapi.model.ResponseUserFollowingItem
import com.example.navigasidanapi.network.RetrofitClient
import com.example.navigasidanapi.viewmodel.ViewModelUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    lateinit var binding : FragmentFollowingBinding
    lateinit var adapter : AdapterFollowing
    lateinit var _adapter : AdapterFollowers
    lateinit var adapter_ : AdapterFollowing
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUser.visibility = View.GONE
        val username = arguments?.getString("nama")

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ViewModelUser::class.java]
        viewModel.allLiveDataFollowing().observe(viewLifecycleOwner, {
            setFollowingData(it as ArrayList<ResponseUserFollowingItem>)
//            adapter.setData(it as ArrayList<ResponseUserFollowingItem>)
//            adapter.notifyDataSetChanged()
        })
        adapter = AdapterFollowing(ArrayList())
        showData(username!!)

    }

    private fun showData(userName : String){
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.getApiService().getFollowing(userName)
            .enqueue(object : Callback<List<ResponseUserFollowingItem>> {
                override fun onResponse(
                    call: Call<List<ResponseUserFollowingItem>>,
                    response: Response<List<ResponseUserFollowingItem>>
                ) {
                    if (response.isSuccessful){
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.visibility = View.VISIBLE
                        binding.rvUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvUser.adapter = AdapterFollowing(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<ResponseUserFollowingItem>>, t: Throwable) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvUser.visibility = View.GONE
                    Toast.makeText(context, "Something Wrong", Toast.LENGTH_LONG).show()
                }
            })
    }
    private fun setFollowerData(data : ArrayList<ResponseUserFollowerItem>){
        _adapter.setData(data)
        adapter.notifyDataSetChanged()
    }
    private fun setFollowingData(data : ArrayList<ResponseUserFollowingItem>){
        adapter_.setData(data)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

}