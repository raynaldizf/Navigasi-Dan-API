package com.example.navigasidanapi.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navigasidanapi.room.Favorite
import com.example.navigasidanapi.R
import com.example.navigasidanapi.databinding.ListUserBinding

class AdapterFavorite(var dataUser : List<Favorite>) : ListAdapter<Favorite, AdapterFavorite.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Favorite> =
            object : DiffUtil.ItemCallback<Favorite>() {
                override fun areItemsTheSame(oldUser: Favorite, newUser: Favorite): Boolean {
                    return oldUser.username == newUser.username
                }

                override fun areContentsTheSame(oldUser: Favorite, newUser: Favorite): Boolean {
                    return oldUser == newUser
                }
            }
    }
    class ViewHolder(val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(dataUser[position].avatar_url).into(holder.binding.images)
        holder.binding.txtNama.text = dataUser[position].username

        holder.binding.card.setOnClickListener {
            holder.binding.card.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("nama", dataUser[position].username)
                bundle.putString("gambar", dataUser[position].avatar_url)
                Navigation.findNavController(it)
                    .navigate(R.id.action_favoritreFragment_to_fragmentDetailUser, bundle)
            }
        }
    }
}