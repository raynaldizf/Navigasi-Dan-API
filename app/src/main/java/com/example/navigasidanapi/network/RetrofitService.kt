package com.example.navigasidanapi.network

import com.example.navigasidanapi.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("search/users")
    fun searchUser(@Query("q") userName: String) : Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") userName: String) : Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") userName: String) : Call<List<ResponseUserFollowerItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") userName: String) : Call<List<ResponseUserFollowingItem>>

    @GET("users")
    fun getAllUser() : Call<List<ResponseDataUserItem>>

}