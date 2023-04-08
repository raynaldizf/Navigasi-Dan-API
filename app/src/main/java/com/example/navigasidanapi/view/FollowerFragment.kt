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
import com.example.navigasidanapi.databinding.FragmentFollowerBinding
import com.example.navigasidanapi.model.ResponseUserFollowerItem
import com.example.navigasidanapi.network.RetrofitClient
import com.example.navigasidanapi.viewmodel.ViewModelUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {
    lateinit var binding : FragmentFollowerBinding
    lateinit var adapter : AdapterFollowers

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowerBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString("nama")

        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ViewModelUser::class.java]
        viewModel.allLiveDataFollowers().observe(viewLifecycleOwner, {
            adapter.setData(it as ArrayList<ResponseUserFollowerItem>)
        })
        adapter = AdapterFollowers(ArrayList())
       showData(username!!)

    }

    private fun showData(userName : String){
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.getApiService().getFollowers(userName)
            .enqueue(object : Callback<List<ResponseUserFollowerItem>>{
                override fun onResponse(
                    call: Call<List<ResponseUserFollowerItem>>,
                    response: Response<List<ResponseUserFollowerItem>>
                ) {
                    if (response.isSuccessful ){
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvUser.adapter = AdapterFollowers(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<ResponseUserFollowerItem>>, t: Throwable) {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(context, "Something Wrong", Toast.LENGTH_LONG).show()
                }
            })
    }
}