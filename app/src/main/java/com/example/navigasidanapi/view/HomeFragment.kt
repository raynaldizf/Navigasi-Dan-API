package com.example.navigasidanapi.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navigasidanapi.R
import com.example.navigasidanapi.adapter.AdapterUser
import com.example.navigasidanapi.adapter.AdapterUserSearch
import com.example.navigasidanapi.databinding.FragmentHomeBinding
import com.example.navigasidanapi.datastore.PreferenceViewModel
import com.example.navigasidanapi.datastore.SettingPreferences
import com.example.navigasidanapi.datastore.ViewModelFactory
import com.example.navigasidanapi.model.Item
import com.example.navigasidanapi.model.ResponseDataUserItem
import com.example.navigasidanapi.model.SearchUserResponse
import com.example.navigasidanapi.network.RetrofitClient
import com.example.navigasidanapi.viewmodel.ViewModelUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    lateinit var adapterUser : AdapterUser
    lateinit var adapterUserSearch: AdapterUserSearch
    private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")
    private var darkmode = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        val pref = SettingPreferences.getInstance(activity.dataStore)
        val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

        prefVM.getThemeSettings().observe(viewLifecycleOwner) { darkActive : Boolean ->
            darkmode = if (!darkActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false
            }
        }


        var viewmodelUser = ViewModelProvider(this).get(ViewModelUser::class.java)
        viewmodelUser.allLiveData().observe(viewLifecycleOwner, {
            adapterUser.setData(it as ArrayList<ResponseDataUserItem>)
        })

        adapterUser = AdapterUser(ArrayList())
        showData()

        adapterUserSearch = AdapterUserSearch(ArrayList())
        binding.rvUser.adapter = adapterUserSearch

        viewmodelUser.allLiveDataSearchUser().observe(viewLifecycleOwner, {
            adapterUserSearch.setData(it as ArrayList<Item>)
        })

        binding.searchboxUser.queryHint = "Search User Here"
        binding.searchboxUser.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.rvUser.visibility = View.GONE
                setSearchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        binding.btnDarkMode.setOnClickListener{
            val pref = SettingPreferences.getInstance(activity.dataStore)
            val prefVM = ViewModelProvider(this, ViewModelFactory(pref))[PreferenceViewModel::class.java]

            if(!darkmode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            prefVM.saveThemeSetting(darkmode)
        }

        binding.btnLove.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_favoritreFragment)
        }
    }

    private fun setSearchUser(query : String){
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.getApiService().searchUser(query)
            .enqueue(object : Callback<SearchUserResponse>{
                override fun onResponse(
                    call: Call<SearchUserResponse>,
                    response: Response<SearchUserResponse>
                ) {
                    if (response.isSuccessful){
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.visibility = View.VISIBLE
                        binding.rvUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvUser.adapter = AdapterUserSearch(response.body()!!.items)
                        Toast.makeText(context, "Load Data Success", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context, "Load Data Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvUser.visibility = View.GONE
                    Toast.makeText(context, "Something Wrong", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun showData(){
        binding.progressBar.visibility = View.VISIBLE
        RetrofitClient.getApiService().getAllUser()
            .enqueue(object : Callback<List<ResponseDataUserItem>>{
                override fun onResponse(
                    call: Call<List<ResponseDataUserItem>>,
                    response: Response<List<ResponseDataUserItem>>
                ) {
                    if (response != null && response.isSuccessful){
                        binding.progressBar.visibility = View.GONE
                        binding.rvUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        binding.rvUser.adapter = AdapterUser(response.body()!!)
                        if (isAdded()) {
                            Toast.makeText(context, "Load Data Success", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        binding.progressBar.visibility = View.VISIBLE
                        if (isAdded()) {
                            Toast.makeText(context, "Load Data Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<ResponseDataUserItem>>, t: Throwable) {
                    if (isAdded()) {
                        Toast.makeText(context, "Something Wrong", Toast.LENGTH_LONG).show()
                    }
                }

            })
    }
}