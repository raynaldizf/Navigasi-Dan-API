package com.example.navigasidanapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigasidanapi.model.*

class ViewModelUser : ViewModel() {
    private val getDataUser = MutableLiveData<List<ResponseDataUserItem>>()
    val _getDataUser :LiveData<List<ResponseDataUserItem>> = getDataUser

    private val getDataUserFollowers = MutableLiveData<List<ResponseUserFollowerItem>>()
    val _getDataUserFollowers : LiveData<List<ResponseUserFollowerItem>> = getDataUserFollowers

    private val getUserDetail = MutableLiveData<UserDetailResponse>()
    val _getUserDetail: LiveData<UserDetailResponse> = getUserDetail

    private val getDataUserFollowing = MutableLiveData<List<ResponseUserFollowingItem>>()
    val _getDataUserFollowing : LiveData<List<ResponseUserFollowingItem>> = getDataUserFollowing

    private val getDataSearchUser = MutableLiveData<List<Item>>()
    val _getDataSearchUser : LiveData<List<Item>> = getDataSearchUser


    fun allLiveData() : LiveData<List<ResponseDataUserItem>> {
        return _getDataUser
    }

    fun allLiveDataFollowers() : LiveData<List<ResponseUserFollowerItem>> {
        return _getDataUserFollowers
    }

    fun allLiveDataFollowing() : LiveData<List<ResponseUserFollowingItem>> {
        return _getDataUserFollowing
    }

    fun allLiveDataSearchUser() : LiveData<List<Item>> {
        return _getDataSearchUser
    }
}