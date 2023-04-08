package com.example.navigasidanapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navigasidanapi.databinding.ListUserBinding
import com.example.navigasidanapi.model.ResponseUserFollowingItem

class AdapterFollowing(var dataUser : List<ResponseUserFollowingItem>) : RecyclerView.Adapter<AdapterFollowing.ViewHolder>() {
    class ViewHolder (val binding : ListUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(dataUser[position].avatarUrl).into(holder.binding.images)
        holder.binding.txtNama.text = dataUser[position].login
    }

    fun setData(data : ArrayList<ResponseUserFollowingItem>){
        this.dataUser = data
    }

}