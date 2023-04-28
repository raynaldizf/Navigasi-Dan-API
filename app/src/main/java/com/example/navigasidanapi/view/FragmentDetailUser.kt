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
      //  (activity as AppCompatActivity).setSupportActionBar(toolbar_name)

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

//        binding.btnFav.setOnClickListener {
//            val item = User(nama, gambar!!, 1)
//
//            // Insert favorite item on a separate thread
//            Thread {
//                AppDatabase.getInstance(requireContext()).userDao().insert(item)
//            }.start()
//
//            Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
//        }


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
//
//                if (isFavorited == 0){
//                    val item = Favorite(nama, gambar!!, 1)
//                    FavDatabase.getInstance(requireContext()).favDao().insertFavs(item)
//                    binding.btnFav.setImageResource(R.drawable.baseline_love_24)
//                    requireActivity().runOnUiThread{
//                        Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
//                    }
//
//                }else if (isFavorited == 1){
//                    val item = Favorite(nama, gambar!!, 0)
//                    FavDatabase.getInstance(requireContext()).favDao().deleteFavs(item)
//                    binding.btnFav.setImageResource(R.drawable.baseline_love_24_outline)
//                    requireActivity().runOnUiThread{
//                        Toast.makeText(requireContext(), "Deleted from Favorites", Toast.LENGTH_SHORT).show()
//                    }
//
//                }
            }.start()

//            userVM._getUserDetail.observe(viewLifecycleOwner) {
//                GlobalScope.async {
//                    if (isFavorited == 1) {
//                        val favorites = Favorite(nama, gambar!!,0)
//                        favVM.deleteFav(favorites)
//                        dao.deleteFavs(favorites)
//
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(context, "Favorite Deleted", Toast.LENGTH_SHORT).show()
//                            Log.e("$favorites", "deleted from favorite")
//                            binding.btnFav.setImageResource(R.drawable.baseline_love_24_outline)
//                        }
//                    }
//                    else if (isFavorited == 0 || isFavorited == null) {
//                        val datafav = Favorite(nama,gambar!!,1)
//                        val favorites = dao.insertFavs(datafav)
//                        favVM.saveFav(datafav)
//
//                        if (favorites != 0L) {
//                            requireActivity().runOnUiThread {
//                                Toast.makeText(
//                                    context,
//                                    "Favorite Added",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                Log.e("$favorites", "added to favorite")
//                                binding.btnFav.setImageResource(R.drawable.baseline_love_24)
//                            }
//                        }
//                        else Toast.makeText(context, "Failed added Favorite", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
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
//
//                        val database = AppDatabase.getInstance(requireContext())
//                        val dao = database.userDao()
//
//                        var isFavorited : Int? = 0
//
//                        GlobalScope.launch(Dispatchers.IO) {
//                            val isFavorited = AppDatabase.getInstance(requireContext()).userDao().isUserFavorited(response.body()?.name!!)
//                            withContext(Dispatchers.Main) {
//                                binding.btnFav.setImageResource(
//                                    if (isFavorited == 1) R.drawable.baseline_love_24 else R.drawable.baseline_love_24_outline
//                                )
//                            }
//                        }

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