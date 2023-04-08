package com.example.navigasidanapi.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navigasidanapi.R
import com.example.navigasidanapi.databinding.ListUserBinding
import com.example.navigasidanapi.model.ResponseDataUserItem

class AdapterUser(var dataUser : List<ResponseDataUserItem>) : RecyclerView.Adapter<AdapterUser.ViewHolder>() {
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
        holder.binding.card.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nama", dataUser[position].login)
            bundle.putString("gambar", dataUser[position].avatarUrl)
            bundle.putString("url", dataUser[position].htmlUrl)
            bundle.putString("id", dataUser[position].id.toString())
            bundle.putString("followers", dataUser[position].followersUrl)
            bundle.putString("following", dataUser[position].followingUrl)
            bundle.putString("repos", dataUser[position].reposUrl)
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_fragmentDetailUser, bundle)
        }
    }

    fun setData(data : ArrayList<ResponseDataUserItem>){
        this.dataUser = data
    }

}