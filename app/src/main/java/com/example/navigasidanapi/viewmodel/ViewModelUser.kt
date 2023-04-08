package com.example.navigasidanapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navigasidanapi.model.Item
import com.example.navigasidanapi.model.ResponseDataUserItem
import com.example.navigasidanapi.model.ResponseUserFollowerItem
import com.example.navigasidanapi.model.ResponseUserFollowingItem

class ViewModelUser : ViewModel() {
    private var getDataUser : MutableLiveData<List<ResponseDataUserItem>?>
    private var getDataUserDetail : MutableLiveData<ResponseDataUserItem?>
    private var getDataUserFollowers : MutableLiveData<List<ResponseUserFollowerItem>?>
    private var getDataUserFollowing : MutableLiveData<List<ResponseUserFollowingItem>?>
    private var getDataSearchUser : MutableLiveData<List<Item>?>


    init {
        getDataUser = MutableLiveData()
        getDataUserDetail = MutableLiveData()
        getDataUserFollowers = MutableLiveData()
        getDataUserFollowing = MutableLiveData()
        getDataSearchUser = MutableLiveData()
    }

    fun allLiveData() : MutableLiveData<List<ResponseDataUserItem>?>{
        return getDataUser
    }

    fun allLiveDataFollowers() : MutableLiveData<List<ResponseUserFollowerItem>?>{
        return getDataUserFollowers
    }

    fun allLiveDataFollowing() : MutableLiveData<List<ResponseUserFollowingItem>?>{
        return getDataUserFollowing
    }

    fun allLiveDataSearchUser() : MutableLiveData<List<Item>?>{
        return getDataSearchUser
    }
}